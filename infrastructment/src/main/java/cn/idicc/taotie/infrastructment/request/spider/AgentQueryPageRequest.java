package cn.idicc.taotie.infrastructment.request.spider;

import lombok.Data;

/**
 * @author zhanghong
 * @date 2025-07-15
 */
@Data
public class AgentQueryPageRequest {

    /**
     * 智能体hash标识
     */
    private Long agentHashId;

    /**
     * 智能体名称
     */
    private String agentName;

    /**
     * 智能体类型，1通用智能体，2预置逻辑智能体
     */
    private Integer agentType;

    /**
     * 启用状态，1启用，0禁用
     */
    private Integer agentStatus;

    private Integer pageNum = 1;

    private Integer pageSize = 10;

}
