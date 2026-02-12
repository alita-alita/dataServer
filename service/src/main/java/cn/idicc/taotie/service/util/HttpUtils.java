package cn.idicc.taotie.service.util;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	protected static final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();


	static {
		connectionManager.setMaxTotal(100);   // 最大连接数
		connectionManager.setDefaultMaxPerRoute(20); // 每个路由最大连接数
	}

	protected static RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(5000)
			.setConnectionRequestTimeout(30000)
			.build();

	protected static HttpClientBuilder httpClientBuilder = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.setDefaultRequestConfig(requestConfig);

	protected static CloseableHttpClient client = httpClientBuilder.build();


	public static String post(String url, String json) {

		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(json, "utf-8"));
		try (CloseableHttpResponse response = client.execute(post)) {
			String responseBody = EntityUtils.toString(response.getEntity());
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode() || HttpStatus.SC_CREATED == response.getStatusLine().getStatusCode()) {
//				logger.info("es index success :{}", responseBody);
				return responseBody;
			} else {
				logger.error("es index, error:{}", responseBody);
				throw new RuntimeException(responseBody);
			}

		} catch (Exception e) {
			logger.error("save error, url:{}, data:{}", url, json, e);
			throw new RuntimeException(e);
		}
	}

	public static String get(String url){
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = client.execute(get)) {
			String responseBody = EntityUtils.toString(response.getEntity());
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode() || HttpStatus.SC_CREATED == response.getStatusLine().getStatusCode()) {
//				logger.info("es index success :{}", responseBody);
				return responseBody;
			} else {
				logger.error("es index, error:{}", responseBody);
				throw new RuntimeException(responseBody);
			}
		} catch (Exception e) {
			logger.error("get error, url:{}", url, e);
			throw new RuntimeException(e);
		}
	}


}
