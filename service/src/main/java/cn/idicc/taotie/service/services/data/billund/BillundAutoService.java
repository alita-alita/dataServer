package cn.idicc.taotie.service.services.data.billund;

import cn.idicc.taotie.service.message.data.KafkaDataMessage;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author: MengDa
 * @Date: 2023/8/15
 * @Description:
 * @version: 1.0
 */
public interface BillundAutoService {

    void consumeMessage(KafkaDataMessage message) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
}
