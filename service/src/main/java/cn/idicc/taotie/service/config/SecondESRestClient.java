//package cn.idicc.taotie.service.config;
//
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
///**
// * @author guyongliang
// */
//@Configuration
//public class SecondESRestClient {
//
//    @Value("${spring.elasticsearch2.rest.uris}")
//    private String userName;
//    @Value("${spring.elasticsearch2.rest.username}")
//    private String password;
//    @Value("${spring.elasticsearch2.rest.password}")
//    private String hostName;
//
//    @Bean(name = "secondEsClient")
//    public RestClient getRestClient() {
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY,
//                new UsernamePasswordCredentials(userName, password));
//        RestClientBuilder restClientBuilder = RestClient.builder(HttpHost.create(hostName));
//        //配置身份验证
//        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder ->
//                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
//        return restClientBuilder.build();
//    }
//}
