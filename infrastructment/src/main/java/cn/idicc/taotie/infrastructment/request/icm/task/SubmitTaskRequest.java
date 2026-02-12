package cn.idicc.taotie.infrastructment.request.icm.task;

import lombok.Data;

import java.util.Map;

/**
 * @Author: MengDa
 * @Date: 2025/2/12
 * @Description:
 * @version: 1.0
 */
@Data
public class SubmitTaskRequest {
    private Long taskId;

    private String remark;

    private String param;
}
