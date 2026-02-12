/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu.impl;


import cn.idicc.taotie.infrastructment.mapper.data.IndustryChainNodeEnterpriseScoreMapper;
import cn.idicc.taotie.infrastructment.dao.data.IndustryChainNodeEnterpriseScoreDao;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeEnterpriseScoreDO;
import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.enums.AmountUnitEnum;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainNodeEnterpriseScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @author wd
 * @version $Id: IndustryChainNodeEnterpriseScoreServiceImpl.java,v 0.1 2020.10.10 auto Exp $
 * @description 产业链节点企业评分信息ServiceImpl
 */
@Slf4j
@Service
public class IndustryChainNodeEnterpriseScoreServiceImpl extends ServiceImpl<IndustryChainNodeEnterpriseScoreMapper, IndustryChainNodeEnterpriseScoreDO> implements IndustryChainNodeEnterpriseScoreService {

    @Autowired
    private IndustryChainNodeEnterpriseScoreDao industryChainNodeEnterpriseScoreDao;

    /**
     * 汇率换算
     *
     * @param registeredCapital
     * @param exchangeRateMap
     * @return
     */
    @Override
    public BigDecimal currencyConverter(String registeredCapital, Map<String, Double> exchangeRateMap) {
        BigDecimal registeredCapitalNew = null;
        BigDecimal tenThousand = new BigDecimal("10000");
        if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(GlobalConstant.UNIT).concat(AmountUnitEnum.CNY.getDesc()))) {
            registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(GlobalConstant.UNIT).concat(AmountUnitEnum.CNY.getDesc())))).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.USD.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.USD.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.USD.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.EUR.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.EUR.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.EUR.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.JPY.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.JPY.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.JPY.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.GBP.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.GBP.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.GBP.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.CHF.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.CHF.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.CHF.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.CAD.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.CAD.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.CAD.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.HKD.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.HKD.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.HKD.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.AUD.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.AUD2.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.AUD.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.SGD.getDesc()))) {
            Double exchangeRate = exchangeRateMap.get(AmountUnitEnum.SGD.getDesc());
            if(exchangeRate != null) {
                registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.SGD.getDesc())))).multiply(BigDecimal.valueOf(exchangeRate)).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
            }
        } else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(GlobalConstant.UNIT))) {
            registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(GlobalConstant.UNIT)))).multiply(tenThousand).setScale(2, RoundingMode.DOWN);
        } else {
            registeredCapitalNew = new BigDecimal("0.00");
        }
        return registeredCapitalNew;
    }

    @Override
    public void batchUpdate(List<IndustryChainNodeEnterpriseScoreDO> list) {
        industryChainNodeEnterpriseScoreDao.updateBatchById(list);
    }
}
