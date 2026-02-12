package cn.idicc.taotie.service.rpc;

import cn.idicc.taotie.infrastructment.entity.spider.AgentDO;
import cn.idicc.taotie.provider.api.common.RPCResult;
import cn.idicc.taotie.provider.api.dto.AgentCard;
import cn.idicc.taotie.provider.api.service.IDICCAgentRPCService;
import cn.idicc.taotie.service.services.data.taoti.AgentService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@DubboService(timeout = 3000)
@Service
public class IDICCAgentRPCServiceImpl implements IDICCAgentRPCService {

	@Autowired
	private AgentService agentService;

	@Override
	public RPCResult<AgentCard> getAgentByKey(String key) {

		AgentDO agentDO = agentService.getOne(Wrappers.lambdaQuery(AgentDO.class)
				.eq(AgentDO::getAgentHashId, key)
				.last("limit 1"));

		if (agentDO != null) {
			AgentCard agentCard = new AgentCard();
			BeanUtils.copyProperties(agentDO, agentCard);
			return new RPCResult<>(agentCard);
		}
		return new RPCResult<>(null);
	}
}
