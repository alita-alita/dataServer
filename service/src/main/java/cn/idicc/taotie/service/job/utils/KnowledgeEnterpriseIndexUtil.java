package cn.idicc.taotie.service.job.utils;

import cn.idicc.taotie.infrastructment.dao.knowledge.KnowledgeEnterpriseRepository;
import cn.idicc.taotie.infrastructment.mapper.data.EnterpriseIndustryLabelRelationMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class KnowledgeEnterpriseIndexUtil {

	private static final Logger logger = LoggerFactory.getLogger(KnowledgeEnterpriseIndexUtil.class);

	@Autowired
	private EnterpriseIndustryLabelRelationMapper enterpriseIndustryLabelRelationMapper;

	@Autowired
	private KnowledgeEnterpriseRepository knowledgeEnterpriseRepository;


	/**
	 * 对比ES和mysql数据
	 * */
	public void compareElasticSearchAndMysqlData(Long chainId){
		//TODO 太粗暴
		List<Long> enterpriseIds = enterpriseIndustryLabelRelationMapper.selectIdByChainId(chainId);
		Set<Long>  idSet         = new HashSet<>(enterpriseIds);
		logger.info("同步企业数量：{}",idSet.size());

		AtomicInteger esCount = new AtomicInteger(0);
		for(Long id : idSet) {
			String esResp = knowledgeEnterpriseRepository.getById(id);
			JsonObject esBody = JsonParser.parseString(esResp).getAsJsonObject();
			JsonObject hitsWrapper = esBody.getAsJsonObject("hits");
			JsonArray  hits        = hitsWrapper.getAsJsonArray("hits");
			if(hits.isEmpty()){
				logger.info("企业ID：{} - {} 不存在", chainId, id);
				continue;
			}
			if(hits.size() > 1){
				logger.info("企业ID：{} - {} 存在多个结果", chainId, id);
			}
			JsonObject source = hits.get(0).getAsJsonObject().get("_source").getAsJsonObject();
			if (source.has("chainIds")) {
				JsonArray chainIds = source.get("chainIds").getAsJsonArray();
				Set<Long> chainIdSet = new HashSet<>();
				for(JsonElement el: chainIds.asList()){
					chainIdSet.add(el.getAsLong());
				}
				if(chainIdSet.contains(chainId)){
					esCount.addAndGet(1);
				} else {
					logger.info("企业ID：{} - {}",id, chainIds);
				}
			}
		}
		logger.info("ES({}) vs mysql({})", esCount.get(), idSet.size());
	}

}
