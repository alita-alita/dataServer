package cn.idicc.taotie.infrastructment.request.spider;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhanghong
 * @date 2025-07-16
 */
public class AgentTestRequest {

    /**
     * 智能体标识
     */
    @NotNull
    private Long agentHashId;

    /**
     * 检测内容
     */
    @NotBlank
    private String message;

}
