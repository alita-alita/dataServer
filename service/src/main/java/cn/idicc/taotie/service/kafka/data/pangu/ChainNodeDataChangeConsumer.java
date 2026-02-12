package cn.idicc.taotie.service.kafka.data.pangu;

import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainNodeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @Author: WangZi
 * @Date: 2023/4/18
 * @Description: 产业链和节点变更同步es消费者
 * @version: 1.0
 */
@Slf4j
@Component
@DependsOn(value = {"namespaceProperties"})
public class ChainNodeDataChangeConsumer {

    @Autowired
    IndustryChainNodeService industryChainNodeService;


    /**
     * 新增产业链节点，数据同步
     * @param record
     * @param ack
     */
    @KafkaListener(topics = {"${namespace.kafka.prefix}" + TopicConstant.SEND_INDUSTRY_NODE_CHANGE_ADVICE})
    public void consumerAddIndustryChainNodeAdvice(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            JSONObject chainNodeChangeDTO = JSON.parseObject(record.value());
            // 同步产业链节点数据到es
            industryChainNodeService.synChainNodeDataToEs(chainNodeChangeDTO);

        } catch (Exception e) {
            log.error("消费企业信息变更通知失败，失败原因为：", e);
            throw e;
        } finally {
            ack.acknowledge();
        }
    }

}
