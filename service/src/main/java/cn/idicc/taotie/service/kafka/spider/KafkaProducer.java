package cn.idicc.taotie.service.kafka.spider;

import cn.idicc.taotie.service.config.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @Autowired
    private KafkaConfig kafkaConfig;

    /**
     * 消息投递。
     * 无视投递结果。
     */
    @Deprecated
    public void emit(String msgKey, String msg) {
//        template.send(kafkaConfig.getSpiderTopic(), msgKey, msg);
    }


}
