package cn.idicc.taotie.infrastructment.request.icm.task;

import lombok.Data;

/**
 * @Author: MengDa
 * @Date: 2025/2/13
 * @Description:
 * @version: 1.0
 */
@Data
public class TaskInstancePageRequest {
    private Long taskId;

    private Long pageNum;

    private Long pageSize;
}
