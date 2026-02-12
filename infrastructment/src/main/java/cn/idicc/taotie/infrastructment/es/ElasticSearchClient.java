package cn.idicc.taotie.infrastructment.es;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
public class ElasticSearchClient {

	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchClient.class);

	protected static final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

	@Value("${spring.elasticsearch2.rest.uris:}")
	private String endpoint;
	@Value("${spring.elasticsearch2.rest.username:}")
	private String username;
	@Value("${spring.elasticsearch2.rest.password:}")
	private String password;

	static {
		connectionManager.setMaxTotal(100);   // 最大连接数
		connectionManager.setDefaultMaxPerRoute(20); // 每个路由最大连接数
	}

	protected RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(5000)
			.setConnectionRequestTimeout(30000)
			.build();

	protected HttpClientBuilder httpClientBuilder = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.setDefaultRequestConfig(requestConfig);

	protected CloseableHttpClient client = httpClientBuilder.build();

	private String createBasicAuthString(String username, String password) {
		String authString = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());
	}

	public void save(String id, String index, JSONObject data) {
		String url = this.endpoint + "/" + index + "/_doc/" + id;
		doSave(url, index, data);
	}

	public void save(String index, JSONObject data) {
		String url = this.endpoint + "/" + index + "/_doc";
		doSave(url, index, data);
	}

	private void doSave(String url, String index, JSONObject data) {
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		post.setEntity(new StringEntity(data.toString(), "utf-8"));
		try (CloseableHttpResponse response = client.execute(post)) {
			String responseBody = EntityUtils.toString(response.getEntity());
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode() || HttpStatus.SC_CREATED == response.getStatusLine().getStatusCode()) {
				logger.info("es index success :{}", responseBody);
			} else {
				logger.error("es index, error:{}", responseBody);
				throw new RuntimeException(responseBody);
			}

		} catch (Exception e) {
			logger.error("save error, index:{}, data:{}", index, data, e);
			throw new RuntimeException(e);
		}
	}


	public void deleteByQuery(JSONObject query, String index) {
		HttpPost post = new HttpPost(this.endpoint + "/" + index + "/_delete_by_query");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		post.setEntity(new StringEntity(query.toString(), "utf-8"));
		try (CloseableHttpResponse response = client.execute(post)) {
			String responseBody = EntityUtils.toString(response.getEntity());
			logger.info(responseBody);
			if (response.getStatusLine().getStatusCode() == 200) {

			} else {
				logger.error("deleteByQuery error, index:{}, query:{}", index, query);
			}

		} catch (Exception e) {
			logger.error("deleteByQuery error, index:{}, query:{}", index, query, e);
		}
	}

	public void deleteById(String index, String id) {
		HttpPost post = new HttpPost(this.endpoint + "/" + index + "/_delete_by_query");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		JSONObject query = new JSONObject();
		JSONObject term  = new JSONObject();
		JSONObject _id   = new JSONObject();
		_id.put("_id", id);
		term.put("term", _id);
		query.put("query", term);
		post.setEntity(new StringEntity(query.toString(), "utf-8"));
		try (CloseableHttpResponse response = client.execute(post)) {
			String responseBody = EntityUtils.toString(response.getEntity());
			logger.info(responseBody);
		} catch (Exception e) {
			logger.error("deleteByQuery error, index:{}, query:{}", index, query, e);
		}
	}

	public String search(String queryString, String index) {
		HttpPost post = new HttpPost(this.endpoint + "/" + index + "/_search");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		post.setEntity(new StringEntity(queryString, "utf-8"));
		try (CloseableHttpResponse response = client.execute(post)) {
			String responseBody = EntityUtils.toString(response.getEntity());
//			logger.info(responseBody);
			if (response.getStatusLine().getStatusCode() == 200) {
				return responseBody;
			} else {
				logger.error("search error, index:{}, query:{}", index, queryString);
				return null;
			}
		} catch (Exception e) {
			logger.error("search error, index:{}, query:{}", index, queryString, e);
			return null;
		}
	}

}
