package cn.idicc.taotie.infrastructment.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: MengDa
 * @Date: 2025/3/25
 * @Description:
 * @version: 1.0
 */
@Component
public class ElasticsearchUtils {

    private ElasticsearchRestTemplate instance;

    private RestHighLevelClient highLevelClient;

    @Value("${spring.elasticsearch2.rest.uris:}")
    private String esUrl;

    @Value("${spring.elasticsearch2.rest.username:}")
    private String esUsername;

    @Value("${spring.elasticsearch2.rest.password:}")
    private String esPassword;

    @PostConstruct
    private void init(){
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esUsername, esPassword));

        RestClientBuilder clientBuilder = RestClient.builder(
                HttpHost.create(esUrl)
        );
        if (esUsername != null && !esUsername.isEmpty() && esPassword != null && !esPassword.isEmpty()) {
            clientBuilder.setHttpClientConfigCallback(e -> {
                e.setDefaultCredentialsProvider(credentialsProvider);
                return e;
            });
        }
        highLevelClient = new RestHighLevelClient(clientBuilder);
        instance = new ElasticsearchRestTemplate(highLevelClient);
    }

    public void save(Object object, String indexName) {

    }

    public ElasticsearchRestTemplate getTemplate() {
        return instance;
    }

    public RestHighLevelClient getClient(){
        return highLevelClient;
    }
}
