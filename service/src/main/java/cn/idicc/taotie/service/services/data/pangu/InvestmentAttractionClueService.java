package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentAttractionClueDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: WangZi
 * @Date: 2023/2/22
 * @Description: 招商线索接口层
 * @version: 1.0
 */
public interface InvestmentAttractionClueService extends IService<InvestmentAttractionClueDO> {
    void doSyncToEs(Long id);
}
