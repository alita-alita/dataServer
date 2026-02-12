package cn.idicc.taotie.infrastructment.response.icm.task;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: MengDa
 * @Date: 2025/2/13
 * @Description:
 * @version: 1.0
 */
@Data
public class TaskInstanceDTO {

    private Long id;
    /**
     * 任务名称
     */
    private Long taskId;

    private String taskName;

    /**
     * 运行备注
     */
    private String instanceDesc;

    /**
     * 参数
     */
    private String param;

    /**
     * 任务状态
     */
    private String status;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtCreate;
}
