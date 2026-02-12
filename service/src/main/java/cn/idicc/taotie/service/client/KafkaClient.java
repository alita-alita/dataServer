package cn.idicc.taotie.service.client;

import org.springframework.kafka.support.SendResult;


/**
 * @Author: WangZi
 * @Date: 2023/4/14
 * @Description:
 * @version: 1.0
 */
public interface KafkaClient {

    /**
     * 同步发送kafka消息
     *
     * @param topic
     * @param jsonStr
     * @return
     */
    SendResult<String, String> sendSync(String topic, String jsonStr);

    /**
     * 同步发送kafka消息
     *
     * @param topic
     * @param key
     * @param jsonStr
     * @return
     */
    SendResult<String, String> sendSync(String topic, String key, String jsonStr);

    /**
     * 异步发送kafka消息
     *
     * @param topic
     * @param key
     * @param jsonStr
     * @return
     */
    SendResult<String, String> sendAnSync(String topic, String key, String jsonStr);

}
