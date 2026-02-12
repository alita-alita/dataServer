package cn.idicc.taotie.api.controller.spider;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.request.spider.SpiderEntity;
import cn.idicc.taotie.infrastructment.utils.MessageKeyUtils;
import cn.idicc.taotie.infrastructment.enums.BusinessTypeEnum;
import cn.idicc.taotie.service.kafka.spider.KafkaProducer;
import cn.idicc.taotie.service.message.spider.MessageEntity;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/spider")
public class SpiderAcceptorController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRelease
    @TokenByPass
    public APIResponse accpet(@Validated @NotNull @RequestBody SpiderEntity spiderEntity) {
        if (!Optional.ofNullable(BusinessTypeEnum.get(spiderEntity.getBusiness())).isPresent()) {
            return APIResponseBuilder.fail(400, "business type not acceptable");
        }
        //发送至消息队列，从cn.idicc.taotie.service.kafka.KafkaConsumer消费处理
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setData(spiderEntity.getData());
        messageEntity.setUrl(spiderEntity.getUrl());
        messageEntity.setSource(spiderEntity.getSource());
        messageEntity.setBusinessType(spiderEntity.getBusiness());
        kafkaProducer.emit(MessageKeyUtils.generateKey(spiderEntity.getUrl()), JSONObject.toJSONString(messageEntity));
        return APIResponseBuilder.success();
    }

}
