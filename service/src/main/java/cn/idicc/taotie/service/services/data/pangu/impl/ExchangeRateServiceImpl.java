package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.idicc.common.util.DateUtil;
import cn.idicc.taotie.infrastructment.mapper.data.ExchangeRateMapper;
import cn.idicc.taotie.infrastructment.entity.data.ExchangeRateDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.service.services.data.pangu.ExchangeRateService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/4/20
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class ExchangeRateServiceImpl extends ServiceImpl<ExchangeRateMapper, ExchangeRateDO> implements ExchangeRateService {

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    @Override
    public Map<String, Double> getExchangeRateMap() {
        List<ExchangeRateDO> exchangeRateList = exchangeRateMapper.selectList(Wrappers.lambdaQuery(ExchangeRateDO.class)
                .eq(ExchangeRateDO::getYear, DateUtil.getCurrentYear())
                .eq(ExchangeRateDO::getDeleted, Boolean.FALSE));
        if (CollectionUtils.isEmpty(exchangeRateList)) {
            throw new BizException("请配置换算汇率");
        }
        return exchangeRateList.stream().collect(Collectors.toMap(ExchangeRateDO::getCurrency, ExchangeRateDO::getExchangeRate));
    }

}
