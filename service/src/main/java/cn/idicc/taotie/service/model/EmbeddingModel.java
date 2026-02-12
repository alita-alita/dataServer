package cn.idicc.taotie.service.model;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.embeddings.TextEmbeddingResultItem;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.protocol.ConnectionConfigurations;
import com.alibaba.dashscope.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/3/28
 * @Description:
 * @version: 1.0
 */
@Component
@Slf4j
public class EmbeddingModel {

    @Autowired
    private LLMProperties llmProperties;

    @Value("${okhttp.config.proxyIp:192.168.0.207}")
    private String PROXY_IP;

    @Value("${okhttp.config.proxyPort:10080}")
    private Integer PROXY_PORT;

    @Value("${okhttp.config.proxyEnable:true}")
    private Boolean PROXY_ENABLE;

    @PostConstruct
    public void init(){
        if (PROXY_ENABLE) {
            Constants.connectionConfigurations = ConnectionConfigurations.builder()
                    .proxyHost(PROXY_IP)
                    .proxyPort(PROXY_PORT)
                    .build();
        }
    }

    public List<TextEmbeddingResultItem> embedding(List<String> input) throws NoApiKeyException {
        TextEmbeddingParam param = TextEmbeddingParam
                .builder()
                .apiKey(llmProperties.getApikey())
                .model(TextEmbedding.Models.TEXT_EMBEDDING_V3)
                .texts(input).build();
        TextEmbedding textEmbedding = new TextEmbedding();
        TextEmbeddingResult result = textEmbedding.call(param);
        return result.getOutput().getEmbeddings();
    }
}
