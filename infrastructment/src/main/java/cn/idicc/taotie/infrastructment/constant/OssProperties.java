package cn.idicc.taotie.infrastructment.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: WangZi
 * @Date: 2023/3/28
 * @Description:
 * @version: 1.0
 */
@Data
@Component
@RefreshScope
public class OssProperties {

    /**
     * 内网地址
     */
    @Value("${oss.internal.endpoint}")
    private String internalEndpoint;

    /**
     * 外网访问地址
     */
    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;
}
