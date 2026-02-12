package cn.idicc.taotie.service.config;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.NamedThreadFactory;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangZi
 * @Date: 2022/12/14
 * @Description: 线程池配置类
 * @version: 1.0
 */
@Configuration
public class ThreadPoolConfig {

    // 核心线程池大小
    private int corePoolSize = 50;

    // 最大可创建的线程数
    private int maxPoolSize = 200;

    // 队列最大长度
    private int queueCapacity = 1000;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;

    @Bean(name = "executor-pangu")
    public ExecutorService executor() {
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 4);
        ThreadPoolExecutor build = ExecutorBuilder.create()
                .setCorePoolSize(cores)
                .setMaxPoolSize(cores * 3)
                .setKeepAliveTime(60, TimeUnit.SECONDS)
                .useArrayBlockingQueue(cores * 1000)
                .setThreadFactory(new NamedThreadFactory("executor-pangu-", false))
                .setAllowCoreThreadTimeOut(true)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .build();
        return build;
    }

    @Bean(name = "executor-pangu-io")
    public ExecutorService executorByIo() {
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 4);
        ThreadPoolExecutor build = ExecutorBuilder.create()
                .setCorePoolSize(cores * 2)
                .setMaxPoolSize(cores * 4)
                .setKeepAliveTime(60, TimeUnit.SECONDS)
                .useArrayBlockingQueue(cores * 1000)
                .setThreadFactory(new NamedThreadFactory("executor-pangu-io-", false))
                .setAllowCoreThreadTimeOut(true)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .build();
        return TtlExecutors.getTtlExecutorService(build);
    }

    @Bean(name = "executor-taotie-okhttp")
    public ExecutorService executorOkhttp() {
        int cores = 10;
        return TtlExecutors.getTtlExecutorService(ExecutorBuilder.create()
                .setCorePoolSize(cores)
                .setMaxPoolSize(cores * 3)
                .setKeepAliveTime(60*3, TimeUnit.SECONDS)
                .useArrayBlockingQueue(cores * 1000)
                .setThreadFactory(new NamedThreadFactory("executor-wenchang-okhttp", false))
                .setAllowCoreThreadTimeOut(true)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .build());
    }

    @Bean(name = "executor-sync-task")
    public ExecutorService executorBySyncTask() {
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 4);
        ThreadPoolExecutor build = ExecutorBuilder.create()
                .setCorePoolSize(cores * 2)
                .setMaxPoolSize(cores * 4)
                .setKeepAliveTime(60, TimeUnit.SECONDS)
                .useArrayBlockingQueue(cores * 1000)
                .setThreadFactory(new NamedThreadFactory("executor-sync-task-", false))
                .setAllowCoreThreadTimeOut(true)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .build();
        return TtlExecutors.getTtlExecutorService(build);
    }

    @Bean(name = "executor-sync-es")
    public ExecutorService executorBySyncES() {
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 10);
        ThreadPoolExecutor build = ExecutorBuilder.create()
                .setCorePoolSize(cores * 2)
                .setMaxPoolSize(cores * 4)
                .setKeepAliveTime(60, TimeUnit.SECONDS)
                .useArrayBlockingQueue(cores * 1000)
                .setThreadFactory(new NamedThreadFactory("executor-sync-es-", false))
                .setAllowCoreThreadTimeOut(true)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .build();
        return TtlExecutors.getTtlExecutorService(build);
    }

    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 4);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores);
        executor.setMaxPoolSize(cores * 2);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("asyncExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean(name = "executor-embedding")
    public ExecutorService executorByEmbedding() {
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 10);
        ThreadPoolExecutor build = ExecutorBuilder.create()
                .setCorePoolSize(cores * 2)
                .setMaxPoolSize(cores * 4)
                .setKeepAliveTime(60, TimeUnit.SECONDS)
                .useArrayBlockingQueue(cores * 1000)
                .setThreadFactory(new NamedThreadFactory("executor-embedding-", false))
                .setAllowCoreThreadTimeOut(true)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .build();
        return TtlExecutors.getTtlExecutorService(build);
    }

    @Bean(name = "executor-icm")
    public ExecutorService icmExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 4);
        ThreadPoolExecutor build = ExecutorBuilder.create()
                .setCorePoolSize(cores)
                .setMaxPoolSize(cores * 3)
                .setKeepAliveTime(60, TimeUnit.SECONDS)
                .useArrayBlockingQueue(cores * 1000)
                .setThreadFactory(new NamedThreadFactory("executor-icm-", false))
                .setAllowCoreThreadTimeOut(true)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .build();
        return build;
    }
}
