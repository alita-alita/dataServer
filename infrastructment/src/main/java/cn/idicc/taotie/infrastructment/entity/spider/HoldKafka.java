package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Data
public class HoldKafka {
    /**
     * 关键字主键id
     */
    private Integer uniKey;
    /**
     * 固定类型
     */
    private String taskCode;
    /**
     *关键词采平台
     */
    private String platform;
    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业统一社会信用代码
     */
    private String uniCode;

    /**
     * 采集时间
     */
    private String currentTime;



    public HoldKafka() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.currentTime = LocalDateTime.now().format(formatter);
    }

}
