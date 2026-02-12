package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.taotie.infrastructment.dto.spider.AgentDTO;
import cn.idicc.taotie.infrastructment.entity.spider.AgentDO;
import cn.idicc.taotie.infrastructment.request.spider.AgentAddRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentQueryPageRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentTestRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentUpdateRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author zhanghong
 * @date 2025-07-16
 */
public interface AgentService extends IService<AgentDO> {

    void save(AgentAddRequest agentDTO);

    void update(AgentUpdateRequest agentDTO);

    void delete(Long id);

    AgentDTO detail(Long id);

    IPage<AgentDTO> getListPage(AgentQueryPageRequest request);

    String testAgent(AgentTestRequest param);
}
