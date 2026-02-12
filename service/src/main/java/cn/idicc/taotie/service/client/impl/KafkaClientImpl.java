package cn.idicc.taotie.service.client.impl;

import cn.hutool.json.JSONUtil;
import cn.idicc.taotie.service.client.KafkaClient;
import cn.idicc.taotie.infrastructment.constant.NamespaceProperties;
import cn.idicc.taotie.infrastructment.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author: WangZi
 * @Date: 2023/4/14
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Component
@RefreshScope
public class KafkaClientImpl implements KafkaClient {

    @Autowired
    private NamespaceProperties namespaceProperties;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public SendResult<String, String> sendSync(String topic, String jsonStr) {
        checkTopic(topic);
        try {
            String newTopic = namespaceProperties.getNamespaceKafkaPrefix() + topic;
            log.info("kafka send message，topic：{}，message：{}", newTopic, jsonStr);
            SendResult<String, String> sendResult = kafkaTemplate.send(newTopic, jsonStr).get(3, TimeUnit.SECONDS);
            return sendResult;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SendResult<String, String> sendSync(String topic, String key, String jsonStr) {
        checkTopic(topic);
        try {
            String newTopic = namespaceProperties.getNamespaceKafkaPrefix() + topic;
            log.info("kafka send message，topic：{}，key：{}，message：{}", newTopic, key, jsonStr);
            SendResult<String, String> sendResult = kafkaTemplate.send(newTopic, key, jsonStr).get(3, TimeUnit.SECONDS);
            return sendResult;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkTopic(String topic) {
        if (StringUtils.isBlank(topic)) {
            throw new BizException("传入topic不能为null");
        }
    }

    @Override
    public SendResult<String, String> sendAnSync(String topic, String key, String jsonStr) {
        checkTopic(topic);
        String newTopic = namespaceProperties.getNamespaceKafkaPrefix() + topic;
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(newTopic, key, jsonStr);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("sendAnSync error,topic is:{},key is:{},jsonStr is:{}，error:", newTopic, key, jsonStr, ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("sendAnSync success,topic is:{},key is:{},jsonStr is:{}，result:{}", newTopic, key, jsonStr, JSONUtil.toJsonStr(result));
            }
        });
        return null;
    }
}
