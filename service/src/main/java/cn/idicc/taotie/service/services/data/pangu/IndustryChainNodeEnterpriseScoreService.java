/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeEnterpriseScoreDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author wd
 * @version $Id: IndustryChainNodeEnterpriseScoreService.java,v 0.1 2020.10.10 auto Exp $
 * @description 产业链节点企业评分信息Service
 */
public interface IndustryChainNodeEnterpriseScoreService extends IService<IndustryChainNodeEnterpriseScoreDO> {

    /**
     * 汇率换算
     *
     * @param registeredCapital
     * @param exchangeRateMap
     * @return
     */
    BigDecimal currencyConverter(String registeredCapital, Map<String, Double> exchangeRateMap);

    /**
     * 批量更新节点企业评分
     *
     * @param list
     */
    void batchUpdate(List<IndustryChainNodeEnterpriseScoreDO> list);
}
