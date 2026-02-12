package cn.idicc.taotie.infrastructment.response.icm;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: MengDa
 * @Date: 2025/1/7
 * @Description:
 * @version: 1.0
 */
@Data
public class RecordVersionDTO {

    /**
     * 版本
     */
    private String version;

    /**
     * 状态
     */
    private Integer state;


    /**
     * 业务下关联键
     */
    private String businessUk;

    /**
     * 业务下关联键
     */
    private String businessRelationKey;

    /**
     * 版本 开始生产时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startProducingDate;

    /**
     * 版本 开始上线时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startOnlineDate;
}
