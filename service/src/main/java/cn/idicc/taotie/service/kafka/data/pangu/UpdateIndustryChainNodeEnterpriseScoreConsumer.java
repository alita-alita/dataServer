package cn.idicc.taotie.service.services.data.pangu.mq;

import cn.idicc.taotie.service.assembler.IndustryChainNodeEnterpriseScoreAssembler;
import cn.idicc.taotie.service.kafka.data.message.UpdateIndustryChainNodeEnterpriseScoreMessage;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainNodeEnterpriseScoreService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wd
 * @description 处理更新节点企业评分消息
 * @date 3/16/23 4:34 PM
 */
@Slf4j
@Component
public class UpdateIndustryChainNodeEnterpriseScoreConsumer {

    @Autowired
    IndustryChainNodeEnterpriseScoreService industryChainNodeEnterpriseScoreService;

    /**
     * TODO 带确认，pangu无生产者，且待确认强行实例化是否受类路径不同影响
     * 订阅更新节点企业评分消息，执行更新操作
     */
    //@KafkaListener(topics = {TopicConstant.UPDATE_CHAIN_NODE_ENTERPRISE_SCORE}, groupId = "group1")
    public void updateNodeEnterpriseScore(ConsumerRecord record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        List<UpdateIndustryChainNodeEnterpriseScoreMessage> values = (List<UpdateIndustryChainNodeEnterpriseScoreMessage>) record.value();
        if (CollectionUtils.isNotEmpty(values)) {
            industryChainNodeEnterpriseScoreService.batchUpdate(IndustryChainNodeEnterpriseScoreAssembler.INSTANCE.messageListToDOs(values));
            ack.acknowledge();
        }
    }
}
