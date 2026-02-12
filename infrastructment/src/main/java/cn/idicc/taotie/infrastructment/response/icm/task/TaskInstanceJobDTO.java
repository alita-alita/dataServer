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
public class TaskInstanceJobDTO {
    /**
     * 任务ID
     */
    private Long taskId;

    private String taskName;
    /**
     * 任务实例ID
     */
    private Long instanceId;

    private String instanceDesc;

    /**
     * python job ID
     */
    private String jobId;

    /**
     * 状态
     */
    private Integer status;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtCreate;
}
