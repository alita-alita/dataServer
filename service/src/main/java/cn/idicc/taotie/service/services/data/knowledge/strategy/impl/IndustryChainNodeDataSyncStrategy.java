package cn.idicc.taotie.service.services.data.knowledge.strategy.impl;

import cn.idicc.taotie.infrastructment.dao.knowledge.KnowledgeIndustryChainNodeRepository;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeIndustryChainNodePO;
import cn.idicc.taotie.service.services.data.knowledge.strategy.KnowledgeDataSyncStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class IndustryChainNodeDataSyncStrategy implements KnowledgeDataSyncStrategy<KnowledgeIndustryChainNodePO> {
    @Autowired
    private KnowledgeIndustryChainNodeRepository knowledgeIndustryChainNodeRepository;

    @Override
    public void dataSync(List<KnowledgeIndustryChainNodePO> industryChainNodePOS) {
        //TODO 逻辑错误，如果节点有删除，ES数据未清理
        //解决方案：遍历es中该节点下的数据，对比存入的pos，其中节点ID应该是对应的，如果发现节点ID存在对应不到，则删除es中该条记录
        knowledgeIndustryChainNodeRepository.deleteStaleEs(industryChainNodePOS);
        knowledgeIndustryChainNodeRepository.saveAll(industryChainNodePOS);
    }
}
