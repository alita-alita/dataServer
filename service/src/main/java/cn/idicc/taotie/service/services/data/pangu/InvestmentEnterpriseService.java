package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: WangZi
 * @Date: 2023/5/10
 * @Description:
 * @version: 1.0
 */
public interface InvestmentEnterpriseService extends IService<InvestmentEnterpriseDO> {

    /**
     * 同步数据到es
     *
     * @param investmentEnterpriseId
     */
    void doSyncToEs(Long investmentEnterpriseId);
}
