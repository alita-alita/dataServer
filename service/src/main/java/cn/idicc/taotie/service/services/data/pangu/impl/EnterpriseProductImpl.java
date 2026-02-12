package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.idicc.taotie.infrastructment.constant.NamespaceProperties;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.EnterpriseProductMapper;
import cn.idicc.taotie.infrastructment.po.data.EnterpriseProductPO;
import cn.idicc.taotie.service.search.EnterpriseProductSearch;
import cn.idicc.taotie.service.services.data.knowledge.strategy.config.KnowledgeStrategyEnum;
import cn.idicc.taotie.service.services.data.knowledge.strategy.context.KnowledgeSyncContext;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseProductService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.management.Query;

@Slf4j
@Service
public class EnterpriseProductImpl implements EnterpriseProductService {

    @Autowired
    private EnterpriseProductSearch enterpriseProductSearch;

    @Autowired
    private EnterpriseProductMapper enterpriseProductMapper;

    @Autowired
    private KnowledgeSyncContext knowledgeSyncContext;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private NamespaceProperties namespaceProperties;

    public void doSyncDataToEs(Long chainId, String uniCode) {
        if (chainId != null ||  uniCode != null) {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (chainId != null) {
                boolQuery.must(QueryBuilders.termQuery("industryChainId", chainId));
            }
            if (uniCode != null) {
                boolQuery.must(QueryBuilders.termQuery("enterprise_uni_code", uniCode));
            }
            NativeSearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .build();
            String indexName = namespaceProperties.getNamespaceRedisPrefix() + "enterprise_product";
            IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
            elasticsearchRestTemplate.delete(query,EnterpriseProductPO.class,indexCoordinates);
            log.info("根据产业链ID删除ES中的产品数据，产业链ID: {}", chainId);
        }
        log.info("开始同步产品ES，产业链id:{}, 企业UniCode为：{}", chainId, uniCode);
        int BATCH_SIZE = 10000;
        Page<EnterpriseProductPO> list;
        int cnt = 1;
        do {
            list = enterpriseProductMapper.getProductByChainIdOrUniCode(
                    new Page<>(cnt++, BATCH_SIZE, false), chainId, uniCode);
//            elasticsearchRestTemplate.save(list.getRecords());
            enterpriseProductSearch.saveAll(list.getRecords());
            //同步到知识库ES
//            knowledgeSyncContext.executeSync(KnowledgeStrategyEnum.ENTERPRISE_PRODUCT_SYNC_STRATEGY, list.getRecords());
        } while (BATCH_SIZE == list.getRecords().size());
    }
}
