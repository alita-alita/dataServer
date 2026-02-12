package cn.idicc.taotie.infrastructment.request.spider;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author zhanghong
 * @date 2025-07-16
 */
@Data
public class AgentUpdateRequest {

    @NotNull
    private Long id;

    /**
     * 智能体类型，1通用智能体，2预置逻辑智能体
     */
    @Max(2)
    @Min(1)
    private Integer agentType;

    /**
     * 启用状态，1启用，0禁用
     */
    @Max(1)
    @Min(0)
    private Integer agentStatus;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 服务地址
     */
    private String mcpServer;

    /**
     * 默认提示词
     */
    private String prompt;

    /**
     * 实现类名称
     */
    private String beanName;

    /**
     * 智能体描述
     */
    private String agentDesc;

}
