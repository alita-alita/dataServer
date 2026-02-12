package cn.idicc.taotie.provider.api.service;

import cn.idicc.taotie.provider.api.common.RPCResult;
import cn.idicc.taotie.provider.api.dto.AgentCard;

public interface IDICCAgentRPCService {

	RPCResult<AgentCard> getAgentByKey(String key);

}
