package cn.idicc.taotie.provider.api.dto;

import java.io.Serializable;

public class AgentCard implements Serializable {

	private Long id;

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
	 * 智能体描述
	 */
	private String agentDesc;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAgentHashId() {
		return agentHashId;
	}

	public void setAgentHashId(Long agentHashId) {
		this.agentHashId = agentHashId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Integer getAgentType() {
		return agentType;
	}

	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}

	public Integer getAgentStatus() {
		return agentStatus;
	}

	public void setAgentStatus(Integer agentStatus) {
		this.agentStatus = agentStatus;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMcpServer() {
		return mcpServer;
	}

	public void setMcpServer(String mcpServer) {
		this.mcpServer = mcpServer;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getAgentDesc() {
		return agentDesc;
	}

	public void setAgentDesc(String agentDesc) {
		this.agentDesc = agentDesc;
	}

	public Integer getIsAsyncMode() {
		return isAsyncMode;
	}

	public void setIsAsyncMode(Integer isAsyncMode) {
		this.isAsyncMode = isAsyncMode;
	}
}
