package cn.idicc.taotie.service.collector;


import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/5/8
 * @Description:
 * @version: 1.0
 */
public interface EnterpriseRelationEsChangeTaskCollector {

    /**
     * 发送企业es索引信息变更，关联企业es数据的其他es做同步处理的通知消息
     *
     * @param enterprisePOS
     */
    void sendEnterpriseRelationEsChangeAdvice(List<EnterprisePO> enterprisePOS);
}
