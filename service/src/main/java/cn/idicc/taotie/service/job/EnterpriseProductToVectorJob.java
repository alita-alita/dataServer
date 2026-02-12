package cn.idicc.taotie.service.job;

import cn.idicc.taotie.infrastructment.entity.xiaoai.EnterpriseProductDO;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.EnterpriseProductMapper;
import cn.idicc.taotie.infrastructment.po.data.EnterpriseProductVectorPO;
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
public class EnterpriseProductToVectorJob {

	private static final Logger logger = LoggerFactory.getLogger(EnterpriseProductToVectorJob.class);

	@Autowired
	private EnterpriseProductMapper enterpriseProductMapper;

	@Value("${server.vector.url:http://192.168.0.24:8002/emb}")
	private String serverVectorURL;

	@Value("${elasticsearch.index.enterpriseProductVector:dev_enterprise_product_vector}")
	private String indexName;

	@Autowired
	private ElasticSearchV8Client elasticSearchV8Client;

	@XxlJob("EnterpriseProductToVectorJob")
	public void doJob() {

		long total = enterpriseProductMapper.selectCount(
				Wrappers.lambdaQuery(EnterpriseProductDO.class)
		);
		if(total == 0){
			return;
		}
		int  pageSize = 1;
		int  handleCount = 0;
		long startId  = 0;

//		String maxIdDSL = createDSLForSearchMaxId();
//		String maxIdResp = elasticSearchV8Client.search(maxIdDSL, indexName);
//		if (maxIdResp != null) {
//			JsonObject jsonObject = JsonParser.parseString(maxIdResp).getAsJsonObject();
//			JsonObject hit = jsonObject.getAsJsonObject("hits");
//			JsonArray hits = hit.getAsJsonArray("hits");
//			if(!hits.isEmpty()){
//				JsonObject source      = hits.get(0).getAsJsonObject().getAsJsonObject("_source");
//				startId = source.get("id").getAsLong();
//			}
//		}
		boolean finish = false;
		do {
			List<EnterpriseProductDO> page = enterpriseProductMapper.selectList(
					Wrappers.lambdaQuery(EnterpriseProductDO.class)
							.gt(EnterpriseProductDO::getId, startId)
							.last("limit " + pageSize)
			);
			if(page.isEmpty()){
				finish = true;
			}
			for (EnterpriseProductDO enterpriseProductDO : page) {
				startId = enterpriseProductDO.getId();
				JsonObject eql = new JsonObject();
				eql.addProperty("productName", enterpriseProductDO.getProductName());

				JsonObject term = new JsonObject();
				term.add("term", eql);
				JsonObject query = new JsonObject();
				query.add("query", term);

				String resp = elasticSearchV8Client.search(query.toString(), indexName);
//			logger.info("queryById:{}", resp);
				if (resp != null) {
					JsonObject jsonObject = JsonParser.parseString(resp).getAsJsonObject();
					JsonObject hitsWrapper = jsonObject.get("hits").getAsJsonObject();
					JsonObject hitTotal = hitsWrapper.get("total").getAsJsonObject();
					long value = hitTotal.get("value").getAsLong();
					if(value != 1){
						if(value > 1) {
							logger.info("{} 重复", enterpriseProductDO.getProductName());
							elasticSearchV8Client.deleteByQuery(query, indexName);
						}
						saveProductVectorToEs(enterpriseProductDO);
					}

				}
			}
			handleCount += page.size();
			logger.info("processing {}/{}, startId:{}", handleCount, total, startId);
		} while (!finish);

		logger.info("处理完成,处理总数:{}", handleCount);

	}

	private void saveProductVectorToEs(EnterpriseProductDO enterpriseProductDO){
		Float[]                   vector   = toVector(enterpriseProductDO.getProductName());
		EnterpriseProductVectorPO vectorPO = new EnterpriseProductVectorPO();
		vectorPO.setId(enterpriseProductDO.getId());
		vectorPO.setProductMd5(enterpriseProductDO.getProductMd5());
		vectorPO.setProductName(enterpriseProductDO.getProductName());
		vectorPO.setVector(vector);
		JSONObject body = JSONObject.from(vectorPO);
		elasticSearchV8Client.save(vectorPO.getId().toString(), indexName, body);
	}

	private String createDSLForSearchMaxId(){
		JsonObject query = new JsonObject();

		// Sort by _id in descending order
		JsonArray sort = new JsonArray();
		JsonObject idSort = new JsonObject();
		JsonObject order = new JsonObject();
		order.addProperty("order", "desc");
		idSort.add("id", order);
		sort.add(idSort);
		query.add("sort", sort);

		// Only need the first record
		query.addProperty("size", 1);

		return query.toString();
	}

	private Float[] toVector(String product) {
		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("text", product);
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
