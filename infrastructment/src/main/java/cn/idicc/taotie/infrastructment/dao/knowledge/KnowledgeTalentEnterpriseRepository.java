package cn.idicc.taotie.infrastructment.dao.knowledge;

import cn.idicc.taotie.infrastructment.constant.NamespaceProperties;
import cn.idicc.taotie.infrastructment.es.ElasticsearchUtils;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeTalentEnterprisePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guyongliang
 * @date 2025-11-10
 */
@Slf4j
@Service
public class KnowledgeTalentEnterpriseRepository {

	private static final String ES_NAME = "knowledge_talent_enterprise";

	@Autowired
	private NamespaceProperties namespaceProperties;

	@Autowired
	private ElasticsearchUtils elasticsearchUtils;

	public void saveAll(List<KnowledgeTalentEnterprisePO> list) {
		IndexCoordinates indexCoordinates = IndexCoordinates.of(namespaceProperties.getNamespaceEsPrefix() + ES_NAME);
		try {
			elasticsearchUtils.getTemplate().save(list, indexCoordinates);
		} catch (Exception e) {
			//DataAccessResourceFailureException，客户端和es版本不匹配可能解析错误
			log.error("ES保存异常,{}", e.getMessage());
		}
	}
}
