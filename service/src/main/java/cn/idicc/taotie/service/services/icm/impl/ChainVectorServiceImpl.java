package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.constant.NamespaceProperties;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryChainMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryChainNodeMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryLabelMapper;
import cn.idicc.taotie.infrastructment.po.icm.IndustryChainProductEmbeddingPO;
import cn.idicc.taotie.infrastructment.response.icm.chain.ChainVectorDTO;
import cn.idicc.taotie.infrastructment.response.icm.chain.IndustryChainProductEmbeddingDTO;
import cn.idicc.taotie.infrastructment.response.icm.chain.NodeChainDTO;
import cn.idicc.taotie.service.model.EmbeddingModel;
import cn.idicc.taotie.infrastructment.es.ElasticsearchUtils;
import cn.idicc.taotie.service.services.icm.ChainVectorService;
import com.alibaba.dashscope.embeddings.TextEmbeddingResultItem;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScriptScoreQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @Author: MengDa
 * @Date: 2025/3/25
 * @Description:
 * @version: 1.0
 */
@Service
@Slf4j
public class ChainVectorServiceImpl implements ChainVectorService {

	@Autowired
	private RecordIndustryChainNodeMapper industryChainNodeMapper;

	@Autowired
	private RecordIndustryChainMapper industryChainMapper;

	@Autowired
	private EmbeddingModel embeddingModel;

	@Autowired
	private ElasticsearchUtils elasticsearchUtils;

	@Autowired
	@Qualifier("executor-embedding")
	private ExecutorService executorService;

	@Autowired
	private NamespaceProperties namespaceProperties;

	private static final String                    ES_NAME = "industry_chain_product_embedding";
	@Autowired
	private              RecordIndustryLabelMapper recordIndustryLabelMapper;

	@Override
	public void buildVector(Long chainId) {

		RecordIndustryChainDO chainDO = industryChainMapper.selectByBizId(chainId);
		Map<Long, RecordIndustryChainNodeDO> nodeMap = industryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getChainId, chainId)
		).stream().collect(Collectors.toMap(RecordIndustryChainNodeDO::getBizId, e -> e));
		if (nodeMap.isEmpty()) {
			throw new BizException("无节点数据");
		}
		List<RecordIndustryChainNodeDO> leafNodeList = nodeMap.values().stream().filter(e -> e.getIsLeaf().equals(1)).collect(Collectors.toList());
		if (leafNodeList.isEmpty()) {
			throw new BizException("无叶子节点数据");
		}
		//构建ES结构数据
		RecordIndustryChainNodeDO rootNode = nodeMap.values().stream().filter(e -> e.getNodeParent().equals(0L)).findFirst().orElseThrow(() -> new BizException("无根节点数据"));
		List<List<NodeChainDTO>>  wayList  = new ArrayList<>();
		buildList(chainId, Collections.singletonList(new NodeChainDTO(rootNode)), wayList);
		List<ChainVectorDTO> vectorDTOList = wayList.stream()
				.map(e -> ChainVectorDTO.from(e, chainDO))
				.flatMap(Collection::stream).collect(Collectors.toList());

		List<List<ChainVectorDTO>>    splitLists = Lists.partition(new ArrayList<>(vectorDTOList), 6);
		List<CompletableFuture<Void>> futureList = new ArrayList<>();
		deleteVector(chainId);
		for (List<ChainVectorDTO> vectorDTOS : splitLists) {
			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				try {
					ElasticsearchRestTemplate     elasticsearchRestTemplate = elasticsearchUtils.getTemplate();
					IndexCoordinates              indexCoordinates          = IndexCoordinates.of(namespaceProperties.getNamespaceEsPrefix() + ES_NAME);
					List<TextEmbeddingResultItem> embeddingResultItems      = embeddingModel.embedding(vectorDTOS.stream().map(ChainVectorDTO::toVectorText).collect(Collectors.toList()));
					if (embeddingResultItems.size() != vectorDTOS.size()) {
						throw new BizException("embeddingResultItems.size() != vectorDTOS.size()");
					}
					for (int i = 0; i < vectorDTOS.size(); i++) {
						TextEmbeddingResultItem         item = embeddingResultItems.get(i);
						IndustryChainProductEmbeddingPO po   = vectorDTOS.get(i).toPo();
						po.setVector(item.getEmbedding().stream().map(Double::floatValue).collect(Collectors.toList()));
						try {
							elasticsearchRestTemplate.save(po, indexCoordinates);
						} catch (Exception e) {
							log.error("ES保存异常,", e);
						}
					}
				} catch (Exception e) {
					log.error("ChainVectorService->buildVector[fail],message:{},e:", vectorDTOS, e);
					throw new BizException("ChainVectorService->embedding Fail:{}", e.getMessage());
				}
			}, executorService);
			futureList.add(future);
		}
		CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
		long errorCount = futureList.stream().filter(CompletableFuture::isCompletedExceptionally).count();
		if (errorCount > 0) {
			log.error("ChainVectorService->buildVector[fail],errorCount:{}", errorCount);
			throw new BizException("存在不正常的向量结果");
		}
	}

	@Override
	public void deleteVector(Long chainId) {
		if (chainId != null) {
			//delete
			ElasticsearchRestTemplate elasticsearchRestTemplate = elasticsearchUtils.getTemplate();
			IndexCoordinates          indexCoordinates          = IndexCoordinates.of(namespaceProperties.getNamespaceEsPrefix() + ES_NAME);
			BoolQueryBuilder          queryBuilder              = new BoolQueryBuilder();
			queryBuilder.must(QueryBuilders.termQuery("chain_id", chainId));
			elasticsearchRestTemplate.delete(
					new NativeSearchQueryBuilder().withQuery(queryBuilder).build(),
					IndustryChainProductEmbeddingPO.class,
					indexCoordinates);
		}
	}

	@Override
	public List<IndustryChainProductEmbeddingDTO> recall(String keyword, Integer limit) {
		try {
			List<TextEmbeddingResultItem> items  = embeddingModel.embedding(Arrays.asList(keyword));
			List<Float>                   vector = items.get(0).getEmbedding().stream().map(Double::floatValue).collect(Collectors.toList());
			return searchByVector(vector, limit);
		} catch (Exception e) {
			log.error("chain vector recall error:", e);
		}
		return Collections.emptyList();
	}

	public List<IndustryChainProductEmbeddingDTO> searchByVector(List<Float> vector, Integer limit) {
		RestHighLevelClient highLevelClient = elasticsearchUtils.getClient();
		Map<String, Object> params          = new HashMap<>(1);
		params.put("query_vector", vector);
		String SORT_SCRIPT = "cosineSimilarity(params.query_vector, 'vector')";
		Script script = new Script(Script.DEFAULT_SCRIPT_TYPE,
				Script.DEFAULT_SCRIPT_LANG,
				SORT_SCRIPT,
				params);
		ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script,
						ScriptSortBuilder.ScriptSortType.NUMBER)
				.order(SortOrder.DESC);
		ScriptScoreQueryBuilder queryBuilder = QueryBuilders.scriptScoreQuery(
				QueryBuilders.boolQuery().must((QueryBuilders.existsQuery("vector"))),
				scriptSortBuilder.script());
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(queryBuilder)
				.fetchSource(new String[]{"id",
						"lib_file_id",
						"chain_id", "chain_name",
						"type",
						"product_name",
						"product_desc",
						"product_way"
				}, null)
				.size(limit);
		SearchRequest searchRequest = new SearchRequest(new String[]{namespaceProperties.getNamespaceEsPrefix() + "industry_chain_product_embedding"}, searchSourceBuilder);

		try {
			SearchResponse                         searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			org.elasticsearch.search.SearchHits    searchHits     = searchResponse.getHits();
			org.elasticsearch.search.SearchHit[]   hits           = searchHits.getHits();
			List<IndustryChainProductEmbeddingDTO> list           = Lists.newArrayList();
			for (org.elasticsearch.search.SearchHit hit : hits) {
				IndustryChainProductEmbeddingDTO embeddingDTO = JSONObject.parseObject(
						hit.getSourceAsString(), IndustryChainProductEmbeddingDTO.class);
				embeddingDTO.setScore(hit.getScore());
				list.add(embeddingDTO);
			}
			return list;
		} catch (IOException e) {
			log.error("industry_chain_product_embedding es embedding search error", e);
		}
		return null;
	}

	private void buildList(Long chainId, List<NodeChainDTO> nodeList, List<List<NodeChainDTO>> res) {
		if (nodeList.isEmpty()) {
			return;
		}
		if (nodeList.get(nodeList.size() - 1).getIsLeaf().equals(1)) {
			//构建Chain
			List<RecordIndustryLabelDO> label    = recordIndustryLabelMapper.getLabelByNodeId(nodeList.get(nodeList.size() - 1).getNodeId());
			List<NodeChainDTO>          nextList = new ArrayList<>(nodeList);
			nextList.get(nextList.size() - 1).setLabelDOList(label);
			res.add(nextList);
		} else {
			res.add(nodeList);
			List<RecordIndustryChainNodeDO> nodeChildren = industryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
					.eq(RecordIndustryChainNodeDO::getChainId, chainId)
					.eq(RecordIndustryChainNodeDO::getNodeParent, nodeList.get(nodeList.size() - 1).getNodeId())
			);
			if (!nodeChildren.isEmpty()) {
				for (RecordIndustryChainNodeDO nodeDO : nodeChildren) {
					List<NodeChainDTO> nextList = new ArrayList<>(nodeList);
					nextList.add(new NodeChainDTO(nodeDO));
					buildList(chainId, nextList, res);
				}
			}
		}
	}
}
