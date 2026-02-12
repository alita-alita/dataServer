package cn.idicc.taotie.service.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ExcelKafkaConfig {
    @Value("${kafka.topic}")
    private String spiderTopic;

    public String getSpiderTopic() {
        return spiderTopic;
    }
}
