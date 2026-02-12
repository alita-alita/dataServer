package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.dto.spider.AgentDTO;
import cn.idicc.taotie.infrastructment.entity.spider.AgentDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.spider.AgentMapper;
import cn.idicc.taotie.infrastructment.request.spider.AgentAddRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentQueryPageRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentTestRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentUpdateRequest;
import cn.idicc.taotie.service.services.data.taoti.AgentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.utils.Crc32;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanghong
 * @date 2025-07-16
 */
@Service
public class AgentServiceImpl extends ServiceImpl<AgentMapper, AgentDO> implements AgentService {

    @Override
    public void save(AgentAddRequest param) {
        AgentDO agentDO = getOne(Wrappers.lambdaQuery(AgentDO.class).eq(AgentDO::getAgentName, param.getAgentName()));
        if (agentDO != null) {
            throw new BizException("智能体名称已存在");
        }

        AgentDO newAgentDO = new AgentDO();
        BeanUtils.copyProperties(param, newAgentDO);
        newAgentDO.setAgentHashId(getNameHashId(newAgentDO.getAgentName()));
        this.save(newAgentDO);
    }

    @Override
    public void update(AgentUpdateRequest param) {
        AgentDO agentDO = getOne(Wrappers.lambdaQuery(AgentDO.class).eq(AgentDO::getId, param.getId()));
        if (agentDO == null) {
            throw new BizException("数据不存在");
        }

        AgentDO modifyAgentDO = new AgentDO();
        BeanUtils.copyProperties(param, modifyAgentDO);
        this.updateById(modifyAgentDO);
    }

    @Override
    public void delete(Long id) {
        AgentDO agentDO = getOne(Wrappers.lambdaQuery(AgentDO.class).eq(AgentDO::getId, id));
        if (agentDO == null) {
            throw new BizException("数据不存在");
        }
        removeById(id);
    }

    @Override
    public AgentDTO detail(Long id) {
        AgentDO agentDO = getOne(Wrappers.lambdaQuery(AgentDO.class).eq(AgentDO::getId, id));
        if (agentDO == null) {
            return null;
        }
        AgentDTO agentDTO = new AgentDTO();
        BeanUtils.copyProperties(agentDO, agentDTO);
        return agentDTO;
    }

    @Override
    public IPage<AgentDTO> getListPage(AgentQueryPageRequest request) {
        Page<AgentDO> tmpPage = this.baseMapper.selectPage(
                new Page<AgentDO>(request.getPageNum(), request.getPageSize()),
                Wrappers.lambdaQuery(AgentDO.class)
                        .eq(StringUtils.isNotBlank(request.getAgentName()), AgentDO::getAgentName, request.getAgentName())
                        .eq(request.getAgentHashId() != null, AgentDO::getAgentHashId, request.getAgentHashId())
                        .eq(request.getAgentType() != null, AgentDO::getAgentType, request.getAgentType())
                        .eq(request.getAgentStatus() != null, AgentDO::getAgentStatus, request.getAgentStatus())
                        .orderByDesc(AgentDO::getGmtModify)
        );
        if (tmpPage == null || tmpPage.getRecords() == null) {
            return new Page<>(request.getPageNum(), request.getPageSize());
        }
        Page<AgentDTO> result = new Page<>(request.getPageNum(), request.getPageSize());
        BeanUtils.copyProperties(tmpPage, result);
        List<AgentDTO> listDtos = new ArrayList<>();
        for (AgentDO record : tmpPage.getRecords()) {
            AgentDTO agentDTO = new AgentDTO();
            BeanUtils.copyProperties(record, agentDTO);
            listDtos.add(agentDTO);
        }
        result.setRecords(listDtos);
        return result;
    }

    @Override
    public String testAgent(AgentTestRequest param) {
        return "";
    }

    private Long getNameHashId(String name) {
        if (StringUtils.isBlank(name)) {
            throw new BizException("名称不能为空");
        }
        return Crc32.crc32(name.getBytes(StandardCharsets.UTF_8));
    }
}
