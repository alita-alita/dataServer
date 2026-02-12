package cn.idicc.taotie.service.collector.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.idicc.taotie.service.client.KafkaClient;
import cn.idicc.taotie.service.collector.EnterpriseRelationEsChangeTaskCollector;
import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/5/8
 * @Description:
 * @version: 1.0
 */
@Component
public class EnterpriseRelationEsChangeTaskCollectorImpl implements EnterpriseRelationEsChangeTaskCollector {

    @Autowired
    private KafkaClient kafkaClient;

    @Override
    public void sendEnterpriseRelationEsChangeAdvice(List<EnterprisePO> enterprisePOS) {
        if (CollectionUtil.isNotEmpty(enterprisePOS)) {
            enterprisePOS.forEach(e -> {
                String unifiedSocialCreditCode = e.getUnifiedSocialCreditCode();
                kafkaClient.sendSync(TopicConstant.SEND_RELATION_ENTERPRISE_CHANGE_ADVICE, unifiedSocialCreditCode, JSONUtil.toJsonStr(e));
            });
        }
    }
}
