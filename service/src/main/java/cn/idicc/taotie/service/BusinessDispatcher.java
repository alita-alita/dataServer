package cn.idicc.taotie.service;

import cn.idicc.taotie.infrastructment.enums.BusinessTypeEnum;
import cn.idicc.taotie.service.message.spider.BusinessHandler;
import cn.idicc.taotie.service.message.spider.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 业务分发器，由此分发器决定业务处理逻辑。
 * 供kafka消费者使用，用于处理不同的消息类型。
 */
@Service
public class BusinessDispatcher {

    @Autowired
    private ApplicationContext context;


    /**
     * 消息分发
     *
     * @param businessType  消息类型
     * @param messageEntity 消息体
     */
    public void dispatch(BusinessTypeEnum businessType, MessageEntity messageEntity) {
        BusinessHandler handler = (BusinessHandler) context.getBean(businessType.getBeanName());
        handler.handle(messageEntity);
    }

}
