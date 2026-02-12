package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.idicc.taotie.infrastructment.mapper.data.EnterpriseInvestmentProportionMapper;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseInvestmentProportionDO;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseInvestmentProportionService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wd
 * @Date: 2023/6/1
 * @Description:产业研发投入serviceImpl
 * @version: 1.0
 */
@Service
public class EnterpriseInvestmentProportionServiceImpl extends ServiceImpl<EnterpriseInvestmentProportionMapper, EnterpriseInvestmentProportionDO> implements EnterpriseInvestmentProportionService {

    @Autowired
    private EnterpriseInvestmentProportionService enterpriseInvestmentProportionService;

    @Override
    public List<EnterpriseInvestmentProportionDO> listByParams(List<String> unifiedSocialCreditCodes) {
        return this.list(Wrappers.lambdaQuery(EnterpriseInvestmentProportionDO.class)
                .in(EnterpriseInvestmentProportionDO::getUnifiedSocialCreditCode, unifiedSocialCreditCodes));
    }
}
