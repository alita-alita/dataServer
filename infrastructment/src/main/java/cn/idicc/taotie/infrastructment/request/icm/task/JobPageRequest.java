package cn.idicc.taotie.infrastructment.request.icm.task;

import lombok.Data;

/**
 * @Author: MengDa
 * @Date: 2025/2/13
 * @Description:
 * @version: 1.0
 */
@Data
public class JobPageRequest {
    private Long taskId;

    private Long instanceId;

    private Long pageNum;

    private Long pageSize;
}
