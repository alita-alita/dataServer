package cn.idicc.taotie.infrastructment.dao.knowledge;

import cn.idicc.taotie.infrastructment.es.ElasticSearchClient;
import cn.idicc.taotie.infrastructment.es.ElasticsearchUtils;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeIndustryChainNodePO;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.redisson.api.StreamMessageId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class KnowledgeIndustryChainNodeRepository {

    @Value("${elasticsearch.index.knowledge_industry_chain_node}")
    private String elasticsearchIndexKnowledgeIndustryChainNode;
    @Autowired
    private ElasticsearchUtils elasticsearchUtils;

    @Autowired
    private ElasticSearchClient elasticSearchClient;


    public void saveAll(List<KnowledgeIndustryChainNodePO> list) {
        try {
            for (KnowledgeIndustryChainNodePO po : list) {
                JSONObject object = JSONObject.from(po);
                elasticSearchClient.save(po.getId().toString(),elasticsearchIndexKnowledgeIndustryChainNode, object);
            }
        } catch (Exception e) {
            //DataAccessResourceFailureException，客户端和es版本不匹配可能解析错误
            log.error("ES保存异常,{}", e);
        }
    }

        /**
         * 删除ES中存在但传入列表中不存在的文档
         */
        public void deleteStaleEs(List<KnowledgeIndustryChainNodePO> industryChainNodePOS) {
            if (industryChainNodePOS == null || industryChainNodePOS.isEmpty()) {
                // 如果传入列表为空，可以选择删除所有文档或什么都不做
                // 根据业务需求决定
                return;
            }
            // 1. 提取传入列表中的所有ID
            Set<String> ids = industryChainNodePOS.stream()
                    .map(KnowledgeIndustryChainNodePO::getId)
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
            Long chainId = industryChainNodePOS.get(0).getChainId();
            // 2. 从ES中查询所有相关文档的ID
            Set<String> existingEsIds = fetchAllDocumentIdsFromEs(chainId);
            // 3. 找出需要删除的文档ID（ES中存在但传入列表中没有）
            Set<String> idsToDelete = existingEsIds.stream()
                    .filter(id -> !ids.contains(id))
                    .collect(Collectors.toSet());
            // 4. 删除这些文档
            if (!idsToDelete.isEmpty()) {
                for (String id : idsToDelete) {
                    log.warn("删除ES中ID为{}的文档",id);
                    elasticSearchClient.deleteById(elasticsearchIndexKnowledgeIndustryChainNode,id);
                }
            }
        }

        /**
         * 从ES中获取所有相关文档的ID
         */
        private Set<String> fetchAllDocumentIdsFromEs(Long chainId) {
            JsonObject queryWrapper = new JsonObject();
            JsonObject query = new JsonObject();
            JsonObject term = new JsonObject();
            term.addProperty("chainId", chainId);
            query.add("term", term);
            queryWrapper.add("query", query);
            String search = elasticSearchClient.search(queryWrapper.toString(), elasticsearchIndexKnowledgeIndustryChainNode);
            Set<String> documentIds = extractIdsFromSearchResult(search);
            log.warn("从ES中获取的ID集合为：{}",documentIds);
            return documentIds;
        }

    // 解析结果获取ID集合
    private Set<String> extractIdsFromSearchResult(String searchResult) {
        Set<String> ids = new HashSet<>();
        if (searchResult != null) {
            try {
                JsonObject result = new JsonParser().parse(searchResult).getAsJsonObject();
                JsonObject hitsObject = result.getAsJsonObject("hits");

                if (hitsObject != null) {
                    JsonArray hitsArray = hitsObject.getAsJsonArray("hits");
                    if (hitsArray != null) {
                        for (JsonElement hitElement : hitsArray) {
                            JsonObject hit = hitElement.getAsJsonObject();
                            String id = hit.get("_id").getAsString();
                            ids.add(id);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("解析搜索结果失败", e);
            }
        }
        return ids;
    }
}
