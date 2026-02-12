package cn.idicc.taotie.service.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
public class LLMProperties {

    @Value("${wenchang.llm.qwen.baseurl:https://dashscope.aliyuncs.com/api/v1}")
    private String baseUrl;

    @Value("${wenchang.llm.qwen.apikey:sk-e59bb34cf3f14d2296526aa27896cf04}")
    private String apikey;

}
