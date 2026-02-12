package cn.idicc.taotie.service.task.logs;

import lombok.Data;

/**
 * @Author: MengDa
 * @Date: 2025/2/12
 * @Description:
 * @version: 1.0
 */
@Data
public class TaskLogDTO {
    private String job_id;

    private String logs;
    private String type;

    private String script_name;

    private Integer sort;
}
