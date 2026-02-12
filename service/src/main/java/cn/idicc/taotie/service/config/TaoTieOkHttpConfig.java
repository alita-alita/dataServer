package cn.idicc.taotie.service.config;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangZi
 * @Date: 2024/3/6
 * @Description:
 * @version: 1.0
 */
@Configuration
public class TaoTieOkHttpConfig {

    @Value("${okhttp.config.proxyIp:192.168.0.207}")
    private String PROXY_IP;

    @Value("${okhttp.config.proxyPort:10080}")
    private Integer PROXY_PORT;

    @Value("${okhttp.config.retry:10}")
    private Integer maxRetry;

    @Resource(name = "executor-taotie-okhttp")
    private ExecutorService executor;

    @Bean("outOkHttpConfig")
    public OkHttpClient outOkHttpConfig(X509TrustManager x509TrustManager,SSLSocketFactory sslSocketFactory,ConnectionPool pool) {
        int connectTimeout = 60;
        int readTimeout = 60;
        int writeTimeout = 60;
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .retryOnConnectionFailure(true)
                .addInterceptor(new RetryInterceptor(maxRetry))
                .connectionPool(pool)
                .dispatcher(new Dispatcher(executor))
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_IP, PROXY_PORT)))
                .build();
    }

    @Bean("commonHttpClient")
    public OkHttpClient commonHttpClient(ConnectionPool pool){
        int connectTimeout = 60;
        int readTimeout = 60;
        int writeTimeout = 60;
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(new RetryInterceptor(maxRetry))
                .connectionPool(pool)
                .dispatcher(new Dispatcher(executor))
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
    }

    public class RetryInterceptor implements Interceptor {
        private final int maxRetryCount;

        public RetryInterceptor(int maxRetryCount) {
            this.maxRetryCount = maxRetryCount;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            int retryCount = 0;
            while (retryCount < maxRetryCount) {
                try {
                    Response response = chain.proceed(request);
                    return response;
                } catch (IOException e) {
                    if (++retryCount == maxRetryCount) {
                        throw e;
                    }
                }
            }

            throw new IOException("Max retry count reached");
        }
    }
}
