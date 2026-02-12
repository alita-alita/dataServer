package cn.idicc.taotie.service.merchants.service;

import cn.idicc.taotie.infrastructment.entity.data.InformationCorrelationChainDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description:
 * @version: 1.0
 */
public interface ZSInformationCorrelationChainService extends IService<InformationCorrelationChainDO> {

    /**
     * 获取指定资讯id和产业链id对应的记录
     *
     * @param informationId
     * @param chainId
     * @return
     */
    InformationCorrelationChainDO getByInformationIdAndChainId(Long informationId, Long chainId);

    /**
     * 查询资讯关联的产业
     *
     * @param informationIds
     * @return
     */
    Map<Long, List<Long>> queryByInformationIds(List<Long> informationIds);
}
