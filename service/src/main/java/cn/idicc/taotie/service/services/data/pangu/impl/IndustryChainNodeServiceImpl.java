package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.idicc.taotie.infrastructment.mapper.data.IndustryChainNodeMapper;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.enums.IndustryChainChangeTypeEnum;
import cn.idicc.taotie.service.search.IndustryChainNodeSearch;
import cn.idicc.taotie.infrastructment.po.data.IndustryChainNodePO;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainNodeService;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @Author: MengDa
 * @Date: 2023/11/21
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
@RefreshScope
public class IndustryChainNodeServiceImpl extends ServiceImpl<IndustryChainNodeMapper, IndustryChainNodeDO> implements IndustryChainNodeService {

    @Autowired
    private IndustryChainService industryChainService;

    @Autowired
    private IndustryChainNodeSearch industryChainNodeSearch;

    @Autowired
    private IndustryChainNodeMapper industryChainNodeMapper;

    @Override
    public void synChainNodeDataToEs(JSONObject chainNodeChangeDTO) {
        Long nodeId = Long.valueOf(chainNodeChangeDTO.get("nodeId").toString());
        Integer changeType = (Integer) chainNodeChangeDTO.get("changeType");
        if (IndustryChainChangeTypeEnum.ADD_NODE.getCode().equals(changeType)
                || IndustryChainChangeTypeEnum.UPDATE_NODE.getCode().equals(changeType)) {
            // 查询产业链节点数据
            IndustryChainNodeDO industryChainNodeDO = this.getById(nodeId);
            if (industryChainNodeDO != null) {
                // 查询节点关联的产业链数据
                IndustryChainDO industryChainDO = industryChainService.getById(industryChainNodeDO.getChainId());
                IndustryChainNodePO nodePO = IndustryChainNodePO.adapt(industryChainNodeDO);
                nodePO.setChainId(industryChainDO.getId());
                nodePO.setChainName(industryChainDO.getChainName());
                industryChainNodeSearch.save(nodePO);
            }
        } else if (IndustryChainChangeTypeEnum.DELETE_NODE.getCode().equals(changeType)) {
            industryChainNodeSearch.deleteById(nodeId);
        }
    }

    /**
     * 获取指定节点的2级节点
     *
     * @param nodeId
     * @return
     */
    @Override
    public IndustryChainNodeDO getSecondNode(Long nodeId) {
        IndustryChainNodeDO industryChainNodeDO = industryChainNodeMapper.selectById(nodeId);
        Assert.notNull(industryChainNodeDO, String.format("传入节点id:%s不存在对应的记录", nodeId));
        Integer nodeLevel = industryChainNodeDO.getNodeLevel();
        if (Integer.valueOf(2).equals(nodeLevel)) {
            return industryChainNodeDO;

        } else if (Integer.valueOf(1).equals(nodeLevel)) {
            return null;

        } else {
            return doGetSecondNode(industryChainNodeDO.getNodeParent());
        }
    }

//    @Override
//    public void getSecondNode(IndustryChainNodeDO node, List<IndustryChainNodeDO> res) {
//        if(!node.getNodeParent().equals(0L)){
//            IndustryChainNodeDO industryChainNodeDO = industryChainNodeMapper.selectById(node.getNodeParent());
//            if(industryChainNodeDO!=null){
//                res.add(industryChainNodeDO);
//                getSecondNode(industryChainNodeDO, res);
//            }
//        }
//    }

    @Override
    public void getParentNode(IndustryChainNodeDO node, List<IndustryChainNodeDO> res) {
        // 边界条件检查
        if (node == null || res == null) {
            return;
        }

        // 使用栈来模拟递归，避免栈溢出
        Deque<IndustryChainNodeDO> stack = new ArrayDeque<>();
        stack.push(node);

        Set<Long> visited = new HashSet<>(); // 防止重复查询

        while (!stack.isEmpty()) {
            IndustryChainNodeDO currentNode = stack.pop();
            Long parentId = currentNode.getNodeParent();

            // 空指针检查
            if (parentId != null && !parentId.equals(0L)) {
                if (!visited.contains(parentId)) {
                    try {
                        IndustryChainNodeDO parent = industryChainNodeMapper.selectById(parentId);
                        if (parent != null) {
                            res.add(parent);
                            stack.push(parent);
                            visited.add(parentId);
                        }
                    } catch (Exception e) {
                        // 异常处理
                        log.error("Failed to select industry chain node by id: " + parentId, e);
                    }
                }
            }
        }
    }

    @Override
    public List<IndustryChainNodeDO> getNodeByChain(Long chainId) {
        ArrayList<Integer> nodelevel = new ArrayList<>();
        nodelevel.add(1);
        nodelevel.add(2);
        nodelevel.add(3);
        List<IndustryChainNodeDO> list = industryChainNodeMapper.selectList(Wrappers.lambdaQuery(IndustryChainNodeDO.class)
                .eq(IndustryChainNodeDO::getChainId, chainId)
                .in(IndustryChainNodeDO::getNodeLevel, nodelevel));
        return ObjectUtils.isNotEmpty(list)?list:Collections.emptyList();
    }

    private IndustryChainNodeDO doGetSecondNode(Long nodeId) {
        IndustryChainNodeDO industryChainNodeDO = industryChainNodeMapper.selectById(nodeId);
        if (Objects.nonNull(industryChainNodeDO)) {
            Integer nodeLevel = industryChainNodeDO.getNodeLevel();
            if (Integer.valueOf(2).equals(nodeLevel)) {
                return industryChainNodeDO;

            } else {
                return doGetSecondNode(industryChainNodeDO.getNodeParent());
            }
        }
        return null;
    }
}
