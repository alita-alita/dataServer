package cn.idicc.taotie.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfig {

    @Value("${system.env}${kafka.topic.spider}")
    private String spiderTopic;

    public String getSpiderTopic() {
        return spiderTopic;
    }

    public void setSpiderTopic(String spiderTopic) {
        this.spiderTopic = spiderTopic;
    }
}
