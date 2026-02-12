package cn.idicc.taotie.infrastructment.dao.knowledge;

import cn.idicc.taotie.infrastructment.constant.NamespaceProperties;
import cn.idicc.taotie.infrastructment.es.ElasticsearchUtils;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeEnterpriseProductPO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * @author guyongliang
 * @date 2025/11/6
 */
@Slf4j
@Service
public class KnowledgeEnterpriseProductRepository {

	private static final String ES_NAME = "knowledge_enterprise_product";

	@Autowired
	private NamespaceProperties namespaceProperties;

	@Autowired
	private ElasticsearchUtils elasticsearchUtils;

	public SearchHits<KnowledgeEnterpriseProductPO> queryByUnicodes(List<String> uniCodes) {
		// 4. 构建ES查询条件
		BoolQueryBuilder boolQuery = boolQuery();
		boolQuery.must(QueryBuilders.termsQuery("unifiedSocialCreditCode", uniCodes));
		NativeSearchQuery build = new NativeSearchQueryBuilder()
				.withQuery(boolQuery)
				.build();
		return elasticsearchUtils.getTemplate()
				.search(build, KnowledgeEnterpriseProductPO.class);
	}

	public void saveAll(List<KnowledgeEnterpriseProductPO> list) {
		IndexCoordinates indexCoordinates = IndexCoordinates.of(namespaceProperties.getNamespaceEsPrefix() + ES_NAME);
		try {
			elasticsearchUtils.getTemplate().save(list, indexCoordinates);
		} catch (Exception e) {
			//DataAccessResourceFailureException，客户端和es版本不匹配可能解析错误
			log.error("ES保存异常,{}", e.getMessage());
		}
	}
}
