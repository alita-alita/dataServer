package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 官网采集 Kafka消息字段
 */
@AllArgsConstructor
@Data
@SuperBuilder
public class OfficialWebsiteKafka {
    /**
     * 主键
     */
    private Integer uniKey;
    /**
     * 采集类型（固定值默认为空）
     */
    private String taskCode;
    /**
     * 采集平台（官网）
     */
    private String platform;
    /**
     * 企业ID
     */
    private String enterpriseId;
    /**
     * 企业名称
     */
    private String enterpriseName;
    /**
     * 企业统一社会信用代码
     */
    private String uniCode;
    /**
     * 企业官网地址
     */
    private String publicAccountUrl;

    /**
     * 采集时间
     */
    private String currentTime;



    public OfficialWebsiteKafka() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.currentTime = LocalDateTime.now().format(formatter);
    }

}
