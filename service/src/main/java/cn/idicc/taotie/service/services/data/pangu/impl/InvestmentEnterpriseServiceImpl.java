package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.idicc.identity.response.InstitutionDTO;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentEnterpriseMapper;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseDO;
import cn.idicc.taotie.service.external.InstitutionService;
import cn.idicc.taotie.service.search.EnterpriseSearch;
import cn.idicc.taotie.service.search.InvestmentEnterpriseSearch;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.data.InvestmentEnterprisePO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentEnterpriseRelationModelService;
import cn.idicc.taotie.service.services.data.pangu.InvestmentEnterpriseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @Author: WangZi
 * @Date: 2023/5/10
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class InvestmentEnterpriseServiceImpl extends ServiceImpl<InvestmentEnterpriseMapper, InvestmentEnterpriseDO> implements InvestmentEnterpriseService {
    @Autowired
    private InvestmentEnterpriseMapper investmentEnterpriseMapper;

    @Autowired
    private EnterpriseSearch enterpriseSearch;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private InvestmentEnterpriseRelationModelService investmentEnterpriseRelationModelService;

    @Autowired
    private InvestmentEnterpriseSearch investmentEnterpriseSearch;

    @Override
    public void doSyncToEs(Long investmentEnterpriseId) {
        InvestmentEnterpriseDO investmentEnterpriseDO = investmentEnterpriseMapper.selectById(investmentEnterpriseId);
        Assert.notNull(investmentEnterpriseDO, String.format("传入招商企业记录id:%s不存在对应的记录", investmentEnterpriseId));
        InvestmentEnterprisePO investmentEnterprisePO = InvestmentEnterprisePO.adapt(investmentEnterpriseDO);
        Optional<EnterprisePO> optional = enterpriseSearch.findById(investmentEnterpriseDO.getEnterpriseId());
        Assert.isTrue(optional.isPresent(), String.format("es中不存在指定id:%s对应的企业记录", investmentEnterpriseDO.getEnterpriseId()));
        EnterprisePO enterprisePO = optional.get();
        InstitutionDTO institutionDTO = institutionService.getById(investmentEnterpriseDO.getOrgId());
        Assert.notNull(institutionDTO, String.format("传入入驻机构id:%s不存在对应的记录", investmentEnterpriseDO.getOrgId()));
        investmentEnterprisePO.setEnterpriseName(enterprisePO.getEnterpriseName());
        investmentEnterprisePO.setUnifiedSocialCreditCode(enterprisePO.getUnifiedSocialCreditCode());
        investmentEnterprisePO.setOrgName(institutionDTO.getOrgName());
        investmentEnterprisePO.setOrgCode(institutionDTO.getOrgCode());
        investmentEnterprisePO.setProvince(enterprisePO.getProvince());
        investmentEnterprisePO.setCity(enterprisePO.getCity());
        investmentEnterprisePO.setArea(enterprisePO.getArea());
        investmentEnterprisePO.setRecommendationReasonDetail(investmentEnterpriseDO.getRecommendationReasonDetail());
        investmentEnterprisePO.setChainIds(enterprisePO.getChainIds());
        investmentEnterprisePO.setEnterpriseLabelIds(enterprisePO.getEnterpriseLabelIds());
        investmentEnterprisePO.setIndustryLabelIds(enterprisePO.getIndustryLabelIds());
        investmentEnterprisePO.setSecondChainNodeIds(enterprisePO.getSecondChainNodeIds());
        investmentEnterprisePO.setSecondChainNodeNames(enterprisePO.getSecondChainNodeNames());
        investmentEnterprisePO.setRegisterDate(enterprisePO.getRegisterDate());
        investmentEnterprisePO.setRegisteredCapital(enterprisePO.getRegisteredCapital());
        investmentEnterprisePO.setInsuredPersonsNumber(enterprisePO.getInsuredPersonsNumber());
        investmentEnterprisePO.setEnterpriseAddress(enterprisePO.getEnterpriseAddress());
        investmentEnterprisePO.setModelTypes(investmentEnterpriseRelationModelService.listTypesByInvestmentEnterpriseId(investmentEnterpriseId));
        investmentEnterpriseSearch.save(investmentEnterprisePO);
//        根据时间刷新,无效手动清空
//        //刷新redis数据
//        String keyName = RedisConstant.ATLAS_INVEST_KEY + investmentEnterpriseDO.getOrgId() + "_" + enterprisePO.getChainIds();
//        panguRedisClient.delete(keyName);
    }

}
