package cn.idicc.taotie.service.rpc;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.dao.icm.RecordIndustryChainNodeDao;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryKbDO;
import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryKbIndustryChainRelationDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.mapper.dw.DwdIndustryKbIndustryChainRelationMapper;
import cn.idicc.taotie.infrastructment.mapper.dw.DwdIndustryKbMapper;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeIndustryChainNodePO;
import cn.idicc.taotie.provider.api.service.ChainNodeSyncRpcService;
import cn.idicc.taotie.service.services.data.knowledge.strategy.config.KnowledgeStrategyEnum;
import cn.idicc.taotie.service.services.data.knowledge.strategy.context.KnowledgeSyncContext;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainNodeLabelRelationService;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainNodeService;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChainNodeSyncRpcServiceImpl implements ChainNodeSyncRpcService {


    @Resource
    private IndustryChainNodeService industryChainNodeService;

    @Resource
    private KnowledgeSyncContext knowledgeSyncContext;

    @Resource
    private IndustryChainService industryChainService;

    @Resource
    private RecordIndustryChainNodeDao recordIndustryChainNodeDao;

    @Resource
    private DwdIndustryKbIndustryChainRelationMapper dwdIndustryKbIndustryChainRelationMapper;

    @Resource
    private IndustryChainNodeLabelRelationService industryChainNodeLabelRelationService;

    @Resource
    private DwdIndustryKbMapper dwdIndustryKbMapper;


    @Override
    public void updateByChain(Long chainId) {
        List<IndustryChainNodeDO> industryChainNodeDOs = industryChainNodeService.getNodeByChain(chainId);
        if (CollectionUtil.isEmpty(industryChainNodeDOs)){
            log.warn("产业链不存在,chainId:{}", chainId);
            return;
        }
        //根据产业链进行知识库更新，每条产业链仅需获取一次概念。
        String chainConcept = getChainConcept(chainId);
        IndustryChainDO chainDO = industryChainService.getById(chainId);
        List<KnowledgeIndustryChainNodePO> knowledgeIndustryChainNodePOS =new ArrayList<>();
        industryChainNodeDOs.forEach(node -> {
            if (ObjectUtils.isEmpty(chainDO)){
                return;
            }
                    KnowledgeIndustryChainNodePO knowledgeIndustryChainNodePO = new KnowledgeIndustryChainNodePO();
                    knowledgeIndustryChainNodePO.setId(node.getId());
                    knowledgeIndustryChainNodePO.setNodeName(node.getNodeName());
                    knowledgeIndustryChainNodePO.setNodeParent(node.getNodeParent());
                    knowledgeIndustryChainNodePO.setNodeLevel(Long.valueOf(node.getNodeLevel()));
                    //获取到节点对应的产品名称
                    List<String> labelName=industryChainNodeLabelRelationService.getLabelByNode(node.getId());
                    knowledgeIndustryChainNodePO.setProductNames(labelName);
                    knowledgeIndustryChainNodePO.setChainId(node.getChainId());

/*                    knowledgeIndustryChainNodePO.setChainName(chainDO.getChainName());
                    knowledgeIndustryChainNodePO.setChainConcept(chainConcept);*/
                    //产业链的介绍 。 产业链节点的介绍
                    //是否为根结点是不同的处理方式，如果是根节点，根据数据组给的表去查询
                    if (node.getNodeLevel().equals(1)){
                        knowledgeIndustryChainNodePO.setNodeConcept(chainConcept);
                    }else {
                        //非根节点时，从recordIndustryChainNode表查询node desc
                        RecordIndustryChainNodeDO recordIndustryChainNodeDO = recordIndustryChainNodeDao.getByNodeId(node.getId());
                        if (recordIndustryChainNodeDO!=null){
                            knowledgeIndustryChainNodePO.setNodeConcept(recordIndustryChainNodeDO.getNodeDesc());
                        }
                    }

                    knowledgeIndustryChainNodePOS.add(knowledgeIndustryChainNodePO);
                });
        //执行同步es方法
        knowledgeSyncContext.executeSync(KnowledgeStrategyEnum.INDUSTRY_CHAIN_NODE_SYNC_STRATEGY,knowledgeIndustryChainNodePOS );
    }

    private String getChainConcept(Long chainId) {
        // 存储产业简介和产业链构成简述
        String chainIntroduction = "";
        /*String industrialChainComposition = "";*/

        // 查询所有相关的产业链关系
        List<DwdIndustryKbIndustryChainRelationDO> relationList =
                dwdIndustryKbIndustryChainRelationMapper.selectList(
                        Wrappers.lambdaQuery(DwdIndustryKbIndustryChainRelationDO.class)
                                .eq(DwdIndustryKbIndustryChainRelationDO::getIndustryChainId, chainId)
                );

        if (!relationList.isEmpty()) {
            // 提取知识库ID
            List<String> kbIds = relationList.stream()
                    .filter(relation ->
                            relation.getKbTitle().endsWith("产业简介") ||
                                    relation.getKbTitle().endsWith("产业链构成简述")
                    )
                    .map(DwdIndustryKbIndustryChainRelationDO::getKbId)
                    .collect(Collectors.toList());

            if (!kbIds.isEmpty()) {
                // 批量查询知识库内容
                List<DwdIndustryKbDO> kbList = dwdIndustryKbMapper.selectList(
                        Wrappers.lambdaQuery(DwdIndustryKbDO.class)
                                .in(DwdIndustryKbDO::getId, kbIds)
                );

                // 构建ID到内容的映射，便于查找
                Map<String, DwdIndustryKbDO> kbMap = kbList.stream()
                        .collect(Collectors.toMap(DwdIndustryKbDO::getId, Function.identity()));

                // 遍历关系列表，提取所需内容
                for (DwdIndustryKbIndustryChainRelationDO relation : relationList) {
                    DwdIndustryKbDO kbItem = kbMap.get(relation.getKbId());
                    if (kbItem != null) {
                        if (kbItem.getTitle().endsWith("产业简介")) {
                            chainIntroduction = StringUtils.defaultString(kbItem.getContent());
                        } /*else if (kbItem.getTitle().endsWith("产业链构成简述")) {
                            industrialChainComposition = StringUtils.defaultString(kbItem.getContent());
                        }*/
                    }
                }
            }
        }
        // 设置节点概念（合并两个内容)
        return  chainIntroduction /*+ industrialChainComposition*/;
    }
}
