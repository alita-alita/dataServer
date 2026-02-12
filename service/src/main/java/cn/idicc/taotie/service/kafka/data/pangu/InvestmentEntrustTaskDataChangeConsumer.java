package cn.idicc.taotie.service.kafka.data.pangu;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentEntrustTaskMapper;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentEntrustTaskDO;
import cn.idicc.taotie.infrastructment.constant.LockConstant;
import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.service.search.InvestmentEntrustTaskSearch;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.data.InvestmentEntrustTaskPO;
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
 * @Date: 2023/5/17
 * @Description: 同步招商委托任务es数据消费者
 * @version: 1.0
 */
@Slf4j
@Component
@DependsOn(value = {"namespaceProperties"})
public class InvestmentEntrustTaskDataChangeConsumer {

    @Autowired
    private InvestmentEntrustTaskMapper investmentEntrustTaskMapper;

    @Autowired
    private InvestmentEntrustTaskSearch investmentEntrustTaskSearch;

    /**
     * 企业数据变更同步
     *
     * @param record
     * @param ack
     */
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Lock(keys = "T(cn.hutool.json.JSONUtil).parseObj(#record.value()).get('unifiedSocialCreditCode')", keyConstant = LockConstant.SYNC_INVESTMENT_ENTRUST_TASK_DATA_TO_ES, lockModel = LockModel.REENTRANT)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_RELATION_ENTERPRISE_CHANGE_ADVICE}, groupId = "InvestmentEntrustTask")
    public void consumerSendRelationEnterpriseChangeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        try {
            EnterprisePO enterprisePO = JSONUtil.toBean(value, EnterprisePO.class);
            Long enterpriseId = enterprisePO.getId();
            List<InvestmentEntrustTaskDO> list = investmentEntrustTaskMapper.selectList(Wrappers.lambdaQuery(InvestmentEntrustTaskDO.class)
                    .eq(InvestmentEntrustTaskDO::getDeleted, Boolean.FALSE)
                    .eq(InvestmentEntrustTaskDO::getEnterpriseId, enterpriseId));
            if (CollectionUtil.isNotEmpty(list)) {
                List<InvestmentEntrustTaskPO> pos = CollectionUtil.newArrayList();
                list.forEach(l -> {
                    Optional<InvestmentEntrustTaskPO> searchPo = investmentEntrustTaskSearch.findById(l.getId());
                    if (searchPo.isPresent()) {
                        InvestmentEntrustTaskPO po = searchPo.get();
                        po.setChainIds(enterprisePO.getChainIds());
                        po.setEnterpriseLabelIds(enterprisePO.getEnterpriseLabelIds());
                        pos.add(po);
                    }
                    if (CollectionUtil.isNotEmpty(pos)) {
                        investmentEntrustTaskSearch.saveAll(pos);
                    }
                });
            }

        } catch (Exception e) {
            log.error("消费招商委托任务变更通知失败，企业信息为：[{}]，失败原因为：", value, e);

        } finally {
            ack.acknowledge();
        }
    }
}
