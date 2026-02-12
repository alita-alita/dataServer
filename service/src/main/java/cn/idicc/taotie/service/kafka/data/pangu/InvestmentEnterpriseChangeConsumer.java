package cn.idicc.taotie.service.kafka.data.pangu;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentEnterpriseMapper;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseDO;
import cn.idicc.taotie.infrastructment.constant.LockConstant;
import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.service.search.InvestmentEnterpriseSearch;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.data.InvestmentEnterprisePO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentEnterpriseService;
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
 * @Date: 2023/5/11
 * @Description: 招商企业数据变更同步es消费者
 * @version: 1.0
 */
@Slf4j
@Component
@DependsOn(value = {"namespaceProperties"})
public class InvestmentEnterpriseChangeConsumer {

    @Autowired
    private InvestmentEnterpriseSearch investmentEnterpriseSearch;

    @Autowired
    private InvestmentEnterpriseMapper investmentEnterpriseMapper;

    @Autowired
    private InvestmentEnterpriseService investmentEnterpriseService;

    /**
     * 企业数据变更同步
     *
     * @param record
     * @param ack
     */
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Lock(keys = "T(cn.hutool.json.JSONUtil).parseObj(#record.value()).get('unifiedSocialCreditCode')", keyConstant = LockConstant.SYNC_INVESTMENT_ENTERPRISE_DATA_TO_ES, lockModel = LockModel.REENTRANT)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_RELATION_ENTERPRISE_CHANGE_ADVICE}, groupId = "InvestmentEnterprise")
    public void consumerSendRelationEnterpriseChangeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        try {
            EnterprisePO enterprisePO = JSONUtil.toBean(value, EnterprisePO.class);
            Long enterpriseId = enterprisePO.getId();
            List<InvestmentEnterpriseDO> list = investmentEnterpriseMapper.selectList(Wrappers.lambdaQuery(InvestmentEnterpriseDO.class)
                    .eq(InvestmentEnterpriseDO::getDeleted, Boolean.FALSE)
                    .eq(InvestmentEnterpriseDO::getEnterpriseId, enterpriseId));
            if (CollectionUtil.isNotEmpty(list)) {
                List<InvestmentEnterprisePO> pos = CollectionUtil.newArrayList();
                list.forEach(l -> {
                    Optional<InvestmentEnterprisePO> searchPo = investmentEnterpriseSearch.findById(l.getId());
                    if (searchPo.isPresent()) {
                        InvestmentEnterprisePO po = searchPo.get();
                        po.setChainIds(enterprisePO.getChainIds());
                        po.setEnterpriseLabelIds(enterprisePO.getEnterpriseLabelIds());
                        po.setIndustryLabelIds(enterprisePO.getIndustryLabelIds());
                        po.setSecondChainNodeIds(enterprisePO.getSecondChainNodeIds());
                        po.setSecondChainNodeNames(enterprisePO.getSecondChainNodeNames());
                        po.setRegisterDate(enterprisePO.getRegisterDate());
                        po.setRegisteredCapital(enterprisePO.getRegisteredCapital());
                        po.setInsuredPersonsNumber(enterprisePO.getInsuredPersonsNumber());
                        po.setEnterpriseAddress(enterprisePO.getEnterpriseAddress());
                        pos.add(po);
                    }
                    if (CollectionUtil.isNotEmpty(pos)) {
                        investmentEnterpriseSearch.saveAll(pos);
                    }
                });
            }

        } catch (Exception e) {
            log.error("消费企业数据变更同步招商企业数据通知失败，企业数据为：[{}]，失败原因为：", value, e);

        } finally {
            ack.acknowledge();
        }
    }

    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Lock(keys = "#record.value()", keyConstant = LockConstant.SYNC_INVESTMENT_ENTERPRISE_DATA_TO_ES, lockModel = LockModel.REENTRANT)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_INVESTMENT_ENTERPRISE_CHANGE_ADVICE})
    public void consumerInvestmentEnterpriseChangeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        try {
            investmentEnterpriseService.doSyncToEs(Long.valueOf(value));

        } catch (Exception e) {
            log.error("消费招商企业变更通知失败，商企业记录id为：[{}]，失败原因为：", value, e);

        } finally {
            ack.acknowledge();
        }
    }
}
