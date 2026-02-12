package cn.idicc.taotie.service.kafka.data.pangu;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentAttractionClueDO;
import cn.idicc.taotie.infrastructment.constant.LockConstant;
import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.service.search.InvestmentAttractionClueSearch;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.data.InvestmentAttractionCluePO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentAttractionClueService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zengtengpeng.annotation.Lock;
import com.zengtengpeng.enums.LockModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Options;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author: WangZi
 * @Date: 2023/5/12
 * @Description: 招商线索数据变更同步es消费者
 * @version: 1.0
 */
@Slf4j
@Component
@DependsOn(value = {"namespaceProperties"})
public class InvestmentAttractionClueChangeConsumer {

    @Autowired
    private InvestmentAttractionClueService investmentAttractionClueService;

    @Autowired
    private InvestmentAttractionClueSearch investmentAttractionClueSearch;


    /**
     * 企业数据变更同步
     *
     * @param record
     * @param ack
     */
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Lock(keys = "T(cn.hutool.json.JSONUtil).parseObj(#record.value()).get('unifiedSocialCreditCode')", keyConstant = LockConstant.SYNC_INVESTMENT_ATTRACTION_CLUE_DATA_TO_ES, lockModel = LockModel.REENTRANT)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_RELATION_ENTERPRISE_CHANGE_ADVICE}, groupId = "InvestmentAttractionClue")
    public void consumerSendRelationEnterpriseChangeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        try {
            EnterprisePO enterprisePO = JSONUtil.toBean(value, EnterprisePO.class);
            Long enterpriseId = enterprisePO.getId();
            List<InvestmentAttractionClueDO> list = investmentAttractionClueService.list(Wrappers.lambdaQuery(InvestmentAttractionClueDO.class)
                    .eq(InvestmentAttractionClueDO::getDeleted, Boolean.FALSE)
                    .eq(InvestmentAttractionClueDO::getEnterpriseId, enterpriseId));
            if (CollectionUtil.isNotEmpty(list)) {
                List<InvestmentAttractionCluePO> pos = CollectionUtil.newArrayList();
                list.forEach(l -> {
                    Optional<InvestmentAttractionCluePO> searchPo = investmentAttractionClueSearch.findById(l.getId());
                    if (searchPo.isPresent()) {
                        InvestmentAttractionCluePO po = searchPo.get();
                        po.setChainIds(enterprisePO.getChainIds());
                        po.setEnterpriseLabelIds(enterprisePO.getEnterpriseLabelIds());
                        po.setIndustryLabelIds(enterprisePO.getIndustryLabelIds());
                        po.setSecondChainNodeIds(enterprisePO.getSecondChainNodeIds());
                        po.setSecondChainNodeNames(enterprisePO.getSecondChainNodeNames());
                        po.setRegisterDate(enterprisePO.getRegisterDate());
                        pos.add(po);
                    }
                    if (CollectionUtil.isNotEmpty(pos)) {
                        investmentAttractionClueSearch.saveAll(pos);
                    }
                });
            }

        } catch (Exception e) {
            log.error("消费企业数据变更同步招商线索数据通知失败，企业信息为：[{}]，失败原因为：", value, e);

        } finally {
            ack.acknowledge();
        }
    }

    @Lock(keys = "#record.value()", keyConstant = LockConstant.SYNC_INVESTMENT_ATTRACTION_CLUE_DATA_TO_ES, lockModel = LockModel.REENTRANT)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_INVESTMENT_ATTRACTION_CLUE_CHANGE_ADVICE})
    public void consumerInvestmentAttractionClueChangeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        try {
            investmentAttractionClueService.doSyncToEs(Long.valueOf(value));

        } catch (Exception e) {
            log.error("消费招商线索变更通知失败，线索id为：[{}]，失败原因为：", value, e);

        } finally {
            ack.acknowledge();
        }
    }
}
