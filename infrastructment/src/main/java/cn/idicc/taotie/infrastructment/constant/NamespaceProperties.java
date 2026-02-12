package cn.idicc.taotie.infrastructment.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: WangZi
 * @Date: 2023/4/14
 * @Description: 环境隔离变量
 * @version: 1.0
 */
@Data
@Component
@RefreshScope
public class NamespaceProperties {

    @Value("${system.env}")
    private String prefix;

    @Value("${namespace.redis.prefix}")
    private String namespaceRedisPrefix;

    @Value("${namespace.es.prefix}")
    private String namespaceEsPrefix;

    @Value("${namespace.kafka.prefix}")
    private String namespaceKafkaPrefix;
}
