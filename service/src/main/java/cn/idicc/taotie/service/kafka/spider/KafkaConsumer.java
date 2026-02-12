package cn.idicc.taotie.service.kafka.spider;

import cn.idicc.taotie.service.BusinessDispatcher;
import cn.idicc.taotie.infrastructment.enums.BusinessTypeEnum;
import cn.idicc.taotie.service.message.spider.MessageEntity;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private BusinessDispatcher businessDispatcher;

//    @KafkaListener(topics = "${system.env}${kafka.topic.spider}")
    public void consume(String msg) {
        logger.info(msg);

        MessageEntity messageEntity = JSONObject.parseObject(msg, MessageEntity.class);

        businessDispatcher.dispatch(BusinessTypeEnum.get(messageEntity.getBusinessType()), messageEntity);

    }

}
