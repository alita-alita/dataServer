package cn.idicc.taotie.service.job;

import cn.idicc.taotie.infrastructment.entity.data.IndustryLabelDO;
import cn.idicc.taotie.infrastructment.mapper.data.IndustryLabelMapper;
import cn.idicc.taotie.infrastructment.po.data.IndustryLabelVectorPO;
import cn.idicc.taotie.service.search.ElasticSearchV8Client;
import cn.idicc.taotie.service.util.HttpUtils;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndustryLabelToVectorJob {

	private static final Logger logger = LoggerFactory.getLogger(IndustryLabelToVectorJob.class);

	@Value("${server.vector.url:http://192.168.0.24:8002/emb}")
	private String serverVectorURL;

	@Value("${elasticsearch.index.industryLabelVector:dev_industry_label_vector}")
	private String indexName;

	@Autowired
	private ElasticSearchV8Client elasticSearchV8Client;

	@Autowired
	private IndustryLabelMapper industryLabelMapper;


	@XxlJob("IndustryLabelToVectorJob")
	public void doJob() {

		int     pageSize    = 100;
		int     handleCount = 0;
		boolean finish      = false;
		long    startId     = 0;
		long total = industryLabelMapper.selectCount(
				Wrappers.lambdaQuery(IndustryLabelDO.class)
		);


		do {
			List<IndustryLabelDO> page = industryLabelMapper.selectList(Wrappers.lambdaQuery(IndustryLabelDO.class)
					.ge(IndustryLabelDO::getId, startId)
					.last("limit " + pageSize));

			if(page.isEmpty()){
				finish = true;
			}
			for (IndustryLabelDO industryLabel : page) {
				startId = industryLabel.getId();
				String resp = elasticSearchV8Client.queryById(indexName, industryLabel.getId().toString());
				if (resp != null) {
					JsonObject jsonObject = JsonParser.parseString(resp).getAsJsonObject();
					if (jsonObject.has("found") && jsonObject.get("found").getAsBoolean()) {
						JsonObject source    = jsonObject.getAsJsonObject("_source");
						String     labelName = source.get("labelName").getAsString();
						if (labelName.equals(industryLabel.getLabelName())) {
							continue;
						}
					}
				}

				Float[] vector = toVector(industryLabel.getLabelName());
				IndustryLabelVectorPO vectorPO = new IndustryLabelVectorPO();
				vectorPO.setId(industryLabel.getId());
				vectorPO.setLabelName(industryLabel.getLabelName());
				vectorPO.setVector(vector);
				JSONObject body = JSONObject.from(vectorPO);
				elasticSearchV8Client.save(vectorPO.getId().toString(), indexName, body);
			}
			handleCount += page.size();
			logger.info("processing {}/{}, startId:{}", handleCount, total, startId);
			if(page.size() < pageSize){
				finish = true;
			}
		} while (!finish);

		logger.info("处理完成,处理总数:{}", handleCount);
	}

	private Float[] toVector(String labelName) {
		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("text", labelName);
		String resp = HttpUtils.post(serverVectorURL, requestBody.toString());
		try {
			JsonArray vectorsResp = JsonParser.parseString(resp).getAsJsonArray();
			Float[]   vector      = new Float[vectorsResp.size()];
			for (int i = 0; i < vectorsResp.size(); i++) {
				vector[i] = vectorsResp.get(i).getAsFloat();
			}
			return vector;
		} catch (Exception e) {
			logger.error("toVector error:{}", e);
		}
		return null;
	}

}
