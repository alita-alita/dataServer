package cn.idicc.taotie.service.kafka.data;

import cn.idicc.taotie.service.message.data.KafkaDataMessage;
import cn.idicc.taotie.service.services.data.billund.BillundAutoService;
import cn.idicc.taotie.service.util.BusinessUtil;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @Author: MengDa
 * @Date: 2023/8/14
 * @Description:
 * @version: 1.0
 */
@Component
public class AutoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AutoConsumer.class);

    @Autowired
    private BillundAutoService billundAutoService;

    @Autowired
    private BusinessUtil businessUtil;

    private void handleData(String msg, Acknowledgment ack){
        try {
            KafkaDataMessage message = JSONObject.parseObject(msg, KafkaDataMessage.class);
//            if (!businessUtil.getBatchLatest(message.getBatch())){
//                return;
//            }
            logger.info("msg:{}", msg);
            billundAutoService.consumeMessage(message);
            businessUtil.addBatchByRedis(message.getBatch());
        }catch (Exception e){
            logger.error(msg+"-->消费失败: ",e);
            logger.info("error:{}", msg);
        }finally {
            ack.acknowledge();
        }
    }
    @KafkaListener(topics = "${system.env}pangu_bus_2")
    public void consume2(String msg, Acknowledgment ack) {
        handleData(msg, ack);
    }

    @KafkaListener(topics = "${system.env}pangu_bus_3")
    public void consume3(String msg, Acknowledgment ack) {
        handleData(msg, ack);
    }

    @KafkaListener(topics = "${system.env}pangu_bus_4")
    public void consume4(String msg, Acknowledgment ack) {
        handleData(msg, ack);
    }

    @KafkaListener(topics = "${system.env}pangu_bus_5")
    public void consume5(String msg, Acknowledgment ack) {
        handleData(msg, ack);
    }
}
