package cn.idicc.taotie.service.kafka.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseDO;
import cn.idicc.taotie.infrastructment.constant.LockConstant;
import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.infrastructment.response.data.EnterpriseDTO;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseService;
import com.zengtengpeng.annotation.Lock;
import com.zengtengpeng.enums.LockModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/4/18
 * @Description: 企业数据变更同步es消费者
 * @version: 1.0
 */
@Slf4j
@Component
@DependsOn(value = {"namespaceProperties"})
public class EnterpriseDataChangeConsumer {

    @Autowired
    private EnterpriseService enterpriseService;

    @Lock(keys = "#record.value()", keyConstant = LockConstant.SYNC_ENTERPRISE_DATA_TO_ES, lockModel = LockModel.REENTRANT)
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_ENTERPRISE_CHANGE_ADVICE})
    public void consumerSendEnterpriseChangeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        try {
            EnterpriseDTO enterpriseDTO = enterpriseService.getByUnifiedSocialCreditCode(value);
            EnterpriseDO enterpriseDO = EnterpriseDTO.adapt(enterpriseDTO);
            List<EnterpriseDO> list = new ArrayList<>();
            list.add(enterpriseDO);
            enterpriseService.doSyncDataToEs(list);
        } catch (Exception e) {
            log.error("消费企业信息变更通知失败，企业社会统一信用代码为：[{}]，失败原因为：", value, e);

        } finally {
            ack.acknowledge();
        }
    }
}
