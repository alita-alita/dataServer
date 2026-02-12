package cn.idicc.taotie.service.search;

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
public class ElasticSearchV8Client {

	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchV8Client.class);

	protected static final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

	@Value("${spring.elasticsearch2.rest.uris:http://es-cn-zky4ax6gi0001yf5m.elasticsearch.aliyuncs.com:9200}")
	private String endpoint;
	@Value("${spring.elasticsearch2.rest.username:elastic}")
	private String username;
	@Value("${spring.elasticsearch2.rest.password:Pa44w0rd_idicc}")
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
//				logger.info("es index success :{}", responseBody);
			} else {
				logger.error("es index, error:{}", responseBody);
				throw new RuntimeException(responseBody);
			}

		} catch (Exception e) {
			logger.error("save error, index:{}, data:{}", index, data, e);
			throw new RuntimeException(e);
		}
	}

	public String queryById(String index, String id) {
		HttpGet get = new HttpGet(this.endpoint + "/" + index + "/_doc/" + id);
		get.setHeader("Content-Type", "application/json");
		get.setHeader("Authorization", createBasicAuthString(this.username, this.password));

		try (CloseableHttpResponse response = client.execute(get)) {
			String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
			if (response.getStatusLine().getStatusCode() == 200) {
				return responseBody;
			} else {
				logger.error("queryById error, index:{}, id:{}, status:{}", index, id, response.getStatusLine().getStatusCode());
				return null;
			}
		} catch (Exception e) {
			logger.error("queryById error, index:{}, id:{}", index, id, e);
			return null;
		}
	}


	public void deleteByQuery(JsonObject query, String index) {
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
			logger.info(responseBody);
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

	private static final List<String> SUPPORTED_FORMAT = Lists.newArrayList("csv", "json", "tsv", "txt", "yaml", "cbor", "smile");

	public String searchBySql(String sql, String format) {
		//format格式校验,csv/json/tsv/txt/yaml/cbor/smile
		String inputFormat = format;
		if (!SUPPORTED_FORMAT.contains(format)) {
			inputFormat = "txt";
		}

		logger.info("es sql:{}, format:{}", sql, inputFormat);
		HttpPost post = new HttpPost(this.endpoint + "/_sql?format=" + inputFormat);
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		post.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		JSONObject query = new JSONObject();
		query.put("query", sql);
		post.setEntity(new StringEntity(query.toString(), "utf-8"));

		try (CloseableHttpResponse response = client.execute(post)) {
			String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info(responseBody);
			if (response.getStatusLine().getStatusCode() == 200) {
				return responseBody;
			} else {
				logger.error("search error, sql:{}, format:{}", sql, format);
				return null;
			}
		} catch (Exception e) {
			logger.error("search error, sql:{}, format:{}", sql, format, e);
			return null;
		}

	}

	public String translateSql(String sql) {
		logger.info("translate es sql:{}", sql);
		HttpPost post = new HttpPost(this.endpoint + "/_sql/translate");
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		post.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		JSONObject query = new JSONObject();
		query.put("query", sql);
		post.setEntity(new StringEntity(query.toString(), "utf-8"));

		try (CloseableHttpResponse response = client.execute(post)) {
			String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info(responseBody);
			if (response.getStatusLine().getStatusCode() == 200) {
				return responseBody;
			} else {
				logger.error("search error, sql:{}", sql);
				return null;
			}
		} catch (Exception e) {
			logger.error("search error, sql:{}", sql, e);
			return null;
		}
	}

	public void createIndex(String indexName, JsonObject mappings) {
		HttpPut put = new HttpPut(this.endpoint + "/" + indexName);
		put.setHeader("Content-Type", "application/json;charset=utf-8");
		put.setHeader("Authorization", createBasicAuthString(this.username, this.password));

		JsonObject settings = getSettings();
		JsonObject json     = new JsonObject();
		json.add("settings", settings);
		mappings.addProperty("dynamic", false);
		json.add("mappings", mappings);
		put.setEntity(new StringEntity(json.toString(), "utf-8"));
		try (CloseableHttpResponse response = client.execute(put)) {
			String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info(responseBody);
			if (response.getStatusLine().getStatusCode() == 200) {
			} else {
				throw new Exception("create index error:" + responseBody);
			}
		} catch (Exception e) {
			logger.error("createIndex error, indexName:{}, mappings:{}", indexName, mappings, e);
			throw new RuntimeException(e);
		}
	}

	public void updateIndex(String indexName, JsonObject mappings) {
		HttpPost put = new HttpPost(this.endpoint + "/" + indexName + "/_mapping");
		put.setHeader("Content-Type", "application/json;charset=utf-8");
		put.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		mappings.addProperty("dynamic", false);
		put.setEntity(new StringEntity(mappings.toString(), "utf-8"));
		try (CloseableHttpResponse response = client.execute(put)) {
			String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info(responseBody);
			if (response.getStatusLine().getStatusCode() == 200) {
			} else {
				throw new Exception("create index error:" + responseBody);
			}
		} catch (Exception e) {
			logger.error("createIndex error, indexName:{}, mappings:{}", indexName, mappings, e);
			throw new RuntimeException(e);
		}
	}

	public void deleteIndex(String indexName) {
		HttpDelete delete = new HttpDelete(this.endpoint + "/" + indexName);
		delete.setHeader("Content-Type", "application/json;charset=utf-8");
		delete.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		try (CloseableHttpResponse response = client.execute(delete)) {
			String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info(responseBody);
			if (response.getStatusLine().getStatusCode() == 200) {
			} else {
				throw new Exception("create index error:" + responseBody);
			}
		} catch (Exception e) {
			logger.error("createIndex error, indexName:{}", indexName, e);
			throw new RuntimeException(e);
		}
	}

	public boolean existsIndex(String indexName) {
		HttpHead head = new HttpHead(this.endpoint + "/" + indexName);
		head.setHeader("Content-Type", "application/json;charset=utf-8");
		head.setHeader("Authorization", createBasicAuthString(this.username, this.password));
		try (CloseableHttpResponse response = client.execute(head)) {
			if (response.getStatusLine().getStatusCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			logger.error("createIndex error, indexName:{}", indexName, e);
		}
		return false;
	}

	private JsonObject getSettings() {
		JsonObject settings = new JsonObject();
		JsonObject index    = new JsonObject();
		index.addProperty("number_of_shards", 1);
		index.addProperty("number_of_replicas", 0);
		settings.add("index", index);
		return settings;
	}

}
