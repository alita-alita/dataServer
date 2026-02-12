package cn.idicc.taotie.infrastructment.dao.knowledge;

import cn.idicc.taotie.infrastructment.es.ElasticSearchClient;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeEnterprisePO;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guyongliang
 * @date 2025/11/6
 */
@Slf4j
@Service
public class KnowledgeEnterpriseRepository {

	@Value("${elasticsearch.index.knowledge_enterprise:dev_knowledge_enterprise}")
	private String elasticsearchIndexKnowledgeEnterprise;

	@Autowired
	private ElasticSearchClient elasticSearchClient;

	public String getById(long id){
		JsonObject queryWrapper = new JsonObject();

		JsonObject query = new JsonObject();
		JsonObject term = new JsonObject();
		term.addProperty("id", id);
		query.add("term", term);
		queryWrapper.add("query", query);

		return elasticSearchClient.search(queryWrapper.toString(), elasticsearchIndexKnowledgeEnterprise);
	}

	public void saveAll(List<KnowledgeEnterprisePO> list) {
		try {
			for (KnowledgeEnterprisePO po : list) {
				JSONObject object = JSONObject.from(po);
				elasticSearchClient.save(po.getId().toString(),elasticsearchIndexKnowledgeEnterprise, object);
			}
		} catch (Exception e) {
			//DataAccessResourceFailureException，客户端和es版本不匹配可能解析错误
			log.error("ES保存异常,{}", e);
		}
	}
}
