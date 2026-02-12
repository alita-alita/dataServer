package cn.idicc.taotie.infrastructment.entity.spider;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zhanghong
 * @date 2025-07-15
 */
@Data
@TableName("agent")
public class AgentDO extends BaseDO {

    /**
     * 智能体hash标识
     * 生成规则数据库crc32(agentName)
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

    /**
     * 是否异步模式，1异步，0同步
     */
    private Integer isAsyncMode;

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
