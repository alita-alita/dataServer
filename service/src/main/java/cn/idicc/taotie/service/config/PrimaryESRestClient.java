//package cn.idicc.taotie.service.config;
//
//import org.apache.http.HttpHost;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author guyongliang
// */
//@Configuration
//public class PrimaryESRestClient {
//
//	@Value("${spring.elasticsearch.rest.uris}")
//	private String hostName;
//
//	@Bean(name = "primaryEsClient")
//	public RestClient getRestClient() {
//		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//		RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(hostName));
//		//配置身份验证
//		restClientBuilder.setHttpClientConfigCallback(httpClientBuilder ->
//				httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
//		return restClientBuilder.build();
//	}
//}
