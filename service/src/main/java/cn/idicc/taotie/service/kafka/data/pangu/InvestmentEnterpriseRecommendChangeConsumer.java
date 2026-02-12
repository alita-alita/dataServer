package cn.idicc.taotie.service.kafka.data.pangu;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentEnterpriseRecommendMapper;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseRecommendDO;
import cn.idicc.taotie.infrastructment.constant.LockConstant;
import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.service.search.InvestmentEnterpriseRecommendSearch;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.data.InvestmentEnterpriseRecommendPO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentEnterpriseRecommendService;
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
 * @Date: 2023/5/8
 * @Description: 同步招商企业推荐记录es数据消费者
 * @version: 1.0
 */
@Slf4j
@Component
@DependsOn(value = {"namespaceProperties"})
public class InvestmentEnterpriseRecommendChangeConsumer {

    @Autowired
    private InvestmentEnterpriseRecommendSearch investmentEnterpriseRecommendSearch;

    @Autowired
    private InvestmentEnterpriseRecommendMapper investmentEnterpriseRecommendMapper;

    @Autowired
    private InvestmentEnterpriseRecommendService investmentEnterpriseRecommendService;

    /**
     * 企业数据变更同步
     *
     * @param record
     * @param ack
     */
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Lock(keys = "T(cn.hutool.json.JSONUtil).parseObj(#record.value()).get('unifiedSocialCreditCode')", keyConstant = LockConstant.SYNC_INVESTMENT_ENTERPRISE_RECOMMEND_DATA_TO_ES, lockModel = LockModel.REENTRANT)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_RELATION_ENTERPRISE_CHANGE_ADVICE}, groupId = "InvestmentEnterpriseRecommend")
    public void consumerSendRelationEnterpriseChangeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        try {
            EnterprisePO enterprisePO = JSONUtil.toBean(value, EnterprisePO.class);
            Long enterpriseId = enterprisePO.getId();
            List<InvestmentEnterpriseRecommendDO> list = investmentEnterpriseRecommendMapper.selectList(Wrappers.lambdaQuery(InvestmentEnterpriseRecommendDO.class)
                    .eq(InvestmentEnterpriseRecommendDO::getDeleted, Boolean.FALSE)
                    .eq(InvestmentEnterpriseRecommendDO::getEnterpriseId, enterpriseId));
            if (CollectionUtil.isNotEmpty(list)) {
                List<InvestmentEnterpriseRecommendPO> pos = CollectionUtil.newArrayList();
                list.forEach(l -> {
                    Optional<InvestmentEnterpriseRecommendPO> searchPo = investmentEnterpriseRecommendSearch.findById(l.getId());
                    if (searchPo.isPresent()) {
                        InvestmentEnterpriseRecommendPO po = searchPo.get();
                        po.setSecondChainNodeIds(enterprisePO.getSecondChainNodeIds());
                        po.setSecondChainNodeNames(enterprisePO.getSecondChainNodeNames());
                        pos.add(po);
                    }
                    if (CollectionUtil.isNotEmpty(pos)) {
                        investmentEnterpriseRecommendSearch.saveAll(pos);
                    }
                });
            }

        } catch (Exception e) {
            log.error("消费招商企业推荐信息变更通知失败，企业信息为：[{}]，失败原因为：", value, e);

        } finally {
            ack.acknowledge();
        }
    }

    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    @Lock(keys = "#record.value()", keyConstant = LockConstant.SYNC_INVESTMENT_ENTERPRISE_RECOMMEND_DATA_TO_ES, lockModel = LockModel.REENTRANT)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_INVESTMENT_ENTERPRISE_RECOMMEND_CHANGE_ADVICE})
    public void consumerInvestmentEnterpriseRecommendChangeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        try {
            investmentEnterpriseRecommendService.doSyncToEs(Long.valueOf(value));

        } catch (Exception e) {
            log.error("消费推荐招商企业信息变更通知失败，招商推荐记录id为：[{}]，失败原因为：", value, e);

        } finally {
            ack.acknowledge();
        }
    }
}
