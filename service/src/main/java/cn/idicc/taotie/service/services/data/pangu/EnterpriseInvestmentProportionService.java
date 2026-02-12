package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseInvestmentProportionDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: wd
 * @Date: 2023/5/24
 * @Description: 产业研发投入service
 * @version: 1.0
 */
public interface EnterpriseInvestmentProportionService extends IService<EnterpriseInvestmentProportionDO> {
    /**
     * 根据条件查询产业研发投入信息
     * @param unifiedSocialCreditCodes
     * @return
     */
    List<EnterpriseInvestmentProportionDO> listByParams(List<String> unifiedSocialCreditCodes);
}
