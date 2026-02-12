package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.ExchangeRateDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Author: WangZi
 * @Date: 2023/4/20
 * @Description:
 * @version: 1.0
 */
public interface ExchangeRateService extends IService<ExchangeRateDO> {

    /**
     * 获取币种汇率map集合
     *
     * @return
     */
    Map<String, Double> getExchangeRateMap();
}
