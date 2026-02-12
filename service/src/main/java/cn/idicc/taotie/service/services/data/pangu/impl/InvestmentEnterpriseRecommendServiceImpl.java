package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.idicc.identity.response.InstitutionDTO;
import cn.idicc.taotie.infrastructment.entity.data.*;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentEnterpriseRecommendMapper;
import cn.idicc.taotie.infrastructment.enums.InvestmentTypeEnum;
import cn.idicc.taotie.infrastructment.enums.UploadFileStatusEnum;
import cn.idicc.taotie.infrastructment.response.data.PersonalInformationDTO;
import cn.idicc.taotie.infrastructment.utils.SnowflakeIdWorker;
import cn.idicc.taotie.service.external.InstitutionService;
import cn.idicc.taotie.infrastructment.response.data.EnterpriseDTO;
import cn.idicc.taotie.infrastructment.response.data.InvestmentEnterpriseRecommendUploadDTO;
import cn.idicc.taotie.service.merchants.service.ZSInformationCorrelationChainService;
import cn.idicc.taotie.service.merchants.service.ZSInformationCorrelationEnterpriseService;
import cn.idicc.taotie.service.merchants.service.ZSInformationService;
import cn.idicc.taotie.service.search.EnterpriseSearch;
import cn.idicc.taotie.service.search.InvestmentEnterpriseRecommendSearch;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.data.InvestmentEnterpriseRecommendPO;
import cn.idicc.taotie.service.services.data.pangu.*;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/5/8
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class InvestmentEnterpriseRecommendServiceImpl extends ServiceImpl<InvestmentEnterpriseRecommendMapper, InvestmentEnterpriseRecommendDO> implements InvestmentEnterpriseRecommendService {

    @Autowired
    private InvestmentEnterpriseRecommendMapper investmentEnterpriseRecommendMapper;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private EnterpriseSearch enterpriseSearch;

    @Autowired
    private InvestmentEnterpriseRecommendSearch investmentEnterpriseRecommendSearch;

    @Autowired
    private UploadFileRecordService uploadFileRecordService;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private OrgIndustryChainRelationService orgIndustryChainRelationService;

    @Autowired
    private PersonalInformationService personalInformationService;

    @Autowired
    private ZSInformationService informationService;

    @Autowired
    private ZSInformationCorrelationEnterpriseService informationCorrelationEnterpriseService;

    @Autowired
    private ZSInformationCorrelationChainService informationCorrelationChainService;

    @Override
    public void doSyncToEs(Long id) {
        InvestmentEnterpriseRecommendDO investmentEnterpriseRecommendDO = investmentEnterpriseRecommendMapper.selectById(id);
        Assert.notNull(investmentEnterpriseRecommendDO, String.format("未查询到指定id:%s的招商推荐记录", id));
        InvestmentEnterpriseRecommendPO po = InvestmentEnterpriseRecommendPO.adapt(investmentEnterpriseRecommendDO);
        Optional<EnterprisePO> optional = enterpriseSearch.findById(investmentEnterpriseRecommendDO.getEnterpriseId());
        Assert.isTrue(optional.isPresent(), String.format("传入企业id:%s不存在对应的记录", investmentEnterpriseRecommendDO.getEnterpriseId()));
        EnterprisePO enterprisePO = optional.get();
        InstitutionDTO institutionDTO = institutionService.getById(investmentEnterpriseRecommendDO.getOrgId());
        Assert.notNull(institutionDTO, String.format("传入机构id:%s不存在对应的记录", investmentEnterpriseRecommendDO.getOrgId()));
        po.setUnifiedSocialCreditCode(enterprisePO.getUnifiedSocialCreditCode());
        po.setEnterpriseName(enterprisePO.getEnterpriseName());
        po.setOrgName(institutionDTO.getOrgName());
        po.setOrgCode(institutionDTO.getOrgCode());
        po.setProvince(enterprisePO.getProvince());
        po.setCity(enterprisePO.getCity());
        po.setArea(enterprisePO.getArea());
        po.setSecondChainNodeIds(enterprisePO.getSecondChainNodeIds());
        po.setSecondChainNodeNames(enterprisePO.getSecondChainNodeNames());
        investmentEnterpriseRecommendSearch.save(po);
    }

    @Override
    public Set<InvestmentEnterpriseRecommendUploadDTO> uploadBatchSave(List<InvestmentEnterpriseRecommendUploadDTO> list, Long uploadFileRecordId) {
        long batchNumber = System.currentTimeMillis();
        Set<InvestmentEnterpriseRecommendUploadDTO> errorList = CollectionUtil.newLinkedHashSet();
        UploadFileRecordDO uploadFileRecordDO = uploadFileRecordService.getById(uploadFileRecordId);
        if (Objects.nonNull(uploadFileRecordDO) && UploadFileStatusEnum.TO_BE_PROCESSED.getCode().equals(uploadFileRecordDO.getStatus())) {
            for (InvestmentEnterpriseRecommendUploadDTO index : list) {
                DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
                transactionDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
                TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
                try {
                    String orgName = index.getOrgName();
                    String enterpriseName = index.getEnterpriseName();
                    String recommendedDate = index.getRecommendedDate();
                    String modelType = index.getType();
                    String recommendationReasonDetail = index.getRecommendationReasonDetail();
                    String relationUserNameOnlyLogo = index.getRelationUserNameOnlyLogo();
                    String associationRelationship = index.getAssociationRelationship();
                    String resourceNeeds = index.getResourceNeeds();
                    String associateLocalEnterpriseCode = index.getAssociateLocalEnterpriseCode();
                    String supplyRelation = index.getSupplyRelation();
                    String associatePolicy = index.getAssociatePolicy();
                    String associateInformationUrl = index.getAssociateInformationUrl();
                    String outsideInvestSatisfaction = index.getOutsideInvestSatisfaction();
                    boolean condition = StringUtils.isNotBlank(orgName) && StringUtils.isNotBlank(enterpriseName) &&
                            StringUtils.isNotBlank(recommendedDate) && StringUtils.isNotBlank(modelType);
                    if (!condition) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    if (StringUtils.isNotBlank(recommendationReasonDetail) && recommendationReasonDetail.length() > 200) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    LocalDateTime recommendedDateParam = DateUtil.Str2LocalDateTime(recommendedDate, DatePattern.NORM_DATETIME_PATTERN);

                    EnterpriseDTO enterpriseDTO = enterpriseService.getByName(enterpriseName);
                    if (Objects.isNull(enterpriseDTO)) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    InstitutionDTO institutionDTO = institutionService.queryByOrgName(orgName);
                    if (Objects.isNull(institutionDTO)) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    InvestmentTypeEnum typeEnum = InvestmentTypeEnum.getByCode(Integer.valueOf(modelType));
                    if (Objects.isNull(typeEnum) || InvestmentTypeEnum.ALL.equals(typeEnum) || InvestmentTypeEnum.ADMIN.equals(typeEnum)) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    Long enterpriseId = enterpriseDTO.getId();
                    Optional<EnterprisePO> optional = enterpriseSearch.findById(enterpriseId);
                    if (!optional.isPresent()) {
                        log.error("当前企业不存在于企业es中,企业id为：{}", enterpriseId);
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    EnterprisePO enterprisePO = optional.get();
                    List<Long> chainIds = enterprisePO.getChainIds();
                    if (CollectionUtil.isEmpty(chainIds)) {
                        log.error("当前企业没有关联的产业链,企业id为：{}", enterpriseId);
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    Long orgId = institutionDTO.getId();
                    List<OrgIndustryChainRelationDO> orgIndustryChainRelationList = orgIndustryChainRelationService.listByOrgId(orgId);
                    if (CollectionUtil.isEmpty(orgIndustryChainRelationList)) {
                        log.error("当前机构不存在关联的产业链,机构id为：{}", orgId);
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    //取当前企业关联产业链和选中入驻机构关联产业链的交集
                    chainIds.retainAll(orgIndustryChainRelationList.stream().map(OrgIndustryChainRelationDO::getIndustryChainId).collect(Collectors.toList()));
                    if (CollectionUtil.isEmpty(chainIds)) {
                        log.error("当前企业和选择入驻机构不存在共同的产业链,企业id为：{}，机构id为：{}", enterpriseId, orgId);
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    PersonalInformationDTO personalInformationDTO = null;
                    EnterpriseDTO relationEnterprise = null;
                    if (InvestmentTypeEnum.PRO_BUSINESS.equals(typeEnum)) {
                        if (StringUtils.isBlank(relationUserNameOnlyLogo) || StringUtils.isBlank(associationRelationship)) {
                            log.error("relationUserNameOnlyLogo或associationRelationship为空");
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }
                        personalInformationDTO = personalInformationService.personalInformation(Integer.valueOf(relationUserNameOnlyLogo));
                        if (Objects.isNull(personalInformationDTO)) {
                            log.error("传入的亲商人唯一标识不存在对应的记录，标识为：{}", relationUserNameOnlyLogo);
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }

                    } else if (InvestmentTypeEnum.RESOURCE.equals(typeEnum)) {
                        if (StringUtils.isBlank(resourceNeeds)) {
                            log.error("resourceNeeds为空");
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }

                    } else if (InvestmentTypeEnum.CHAIN_OWNER.equals(typeEnum)) {
                        if (StringUtils.isBlank(associateLocalEnterpriseCode) || StringUtils.isBlank(supplyRelation)) {
                            log.error("associateLocalEnterpriseCode或supplyRelation为空");
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }
                        relationEnterprise = enterpriseService.getByUnifiedSocialCreditCode(associateLocalEnterpriseCode);
                        if (Objects.isNull(relationEnterprise)) {
                            log.error("传入的关联本地企业社会统一信用代码不存在对应的记录，值为：{}", associateLocalEnterpriseCode);
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }

                    } else if (InvestmentTypeEnum.POLICY.equals(typeEnum)) {
                        if (StringUtils.isBlank(associatePolicy)) {
                            log.error("associatePolicy为空");
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }

                    } else if (InvestmentTypeEnum.INFORMATION.equals(typeEnum)) {
                        if (StringUtils.isBlank(associateInformationUrl)) {
                            log.error("associateInformationUrl为空");
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }
                        InformationDO informationDO = informationService.getByUrl(associateInformationUrl);
                        if (Objects.isNull(informationDO)) {
                            log.error("传入的关联资讯url不存在对应的记录，值为：{}", associateInformationUrl);
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }

                    } else if (InvestmentTypeEnum.AI.equals(typeEnum)) {
                        if (StringUtils.isBlank(outsideInvestSatisfaction)) {
                            log.error("outsideInvestSatisfaction为空");
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }
                    }


                    InvestmentEnterpriseRecommendDO adapt = new InvestmentEnterpriseRecommendDO();
                    Long id = SnowflakeIdWorker.nextId();
                    adapt.setId(id);
                    adapt.setOrgId(institutionDTO.getId());
                    adapt.setEnterpriseId(enterpriseDTO.getId());
                    adapt.setRecommendedDate(recommendedDateParam);
                    adapt.setType(Integer.valueOf(modelType));
                    adapt.setRecommendationReasonDetail(recommendationReasonDetail);
                    adapt.setBatchNumber(String.valueOf(batchNumber));
                    String userName = uploadFileRecordDO.getCreateBy();
                    adapt.setCreateBy(userName);
                    adapt.setUpdateBy(userName);
                    if (InvestmentTypeEnum.PRO_BUSINESS.equals(typeEnum)) {
                        adapt.setRelationUserName(personalInformationDTO.getName());
                        adapt.setRelationUserNameOnlyLogo(relationUserNameOnlyLogo);
                        adapt.setAssociationRelationship(associationRelationship);

                    } else if (InvestmentTypeEnum.RESOURCE.equals(typeEnum)) {
                        adapt.setResourceNeeds(resourceNeeds);

                    } else if (InvestmentTypeEnum.CHAIN_OWNER.equals(typeEnum)) {
                        adapt.setAssociateLocalEnterpriseName(relationEnterprise.getEnterpriseName());
                        adapt.setAssociateLocalEnterpriseCode(associateLocalEnterpriseCode);
                        adapt.setSupplyRelation(supplyRelation);

                    } else if (InvestmentTypeEnum.POLICY.equals(typeEnum)) {
                        adapt.setAssociatePolicy(associatePolicy);

                    } else if (InvestmentTypeEnum.INFORMATION.equals(typeEnum)) {
                        adapt.setAssociateInformationUrl(associateInformationUrl);

                    } else if (InvestmentTypeEnum.AI.equals(typeEnum)) {
                        adapt.setOutsideInvestSatisfaction(outsideInvestSatisfaction);
                    }
                    investmentEnterpriseRecommendMapper.insert(adapt);
                    if (InvestmentTypeEnum.INFORMATION.equals(typeEnum)) {
                        syncInformation(adapt);
                    }
                    doSyncToEs(id);
                    platformTransactionManager.commit(transactionStatus);

                } catch (Exception e) {
                    errorList.add(index);
                    log.error("InvestmentEnterpriseRecommendServiceImpl -> uploadBatchSave出现异常：", e);
                    platformTransactionManager.rollback(transactionStatus);
                }
            }
        }
        return errorList;
    }

    public void syncInformation(InvestmentEnterpriseRecommendDO adapt) {
        InformationCorrelationEnterpriseDO informationCorrelationEnterpriseDO = new InformationCorrelationEnterpriseDO();
        Optional<EnterprisePO> optional = enterpriseSearch.findById(adapt.getEnterpriseId());
        Assert.isTrue(optional.isPresent(), String.format("es中不存在传入企业id为[%s]的数据", adapt.getEnterpriseId()));
        EnterprisePO enterprisePO = optional.get();
        InformationDO informationDO = informationService.getByUrl(adapt.getAssociateInformationUrl());
        InformationCorrelationEnterpriseDO informationCorrelationEnterpriseOld = informationCorrelationEnterpriseService.getByInformationIdAndUnifiedSocialCreditCode(informationDO.getId(), enterprisePO.getUnifiedSocialCreditCode());
        if (Objects.isNull(informationCorrelationEnterpriseOld)) {
            informationCorrelationEnterpriseDO.setInformationId(informationDO.getId());
            informationCorrelationEnterpriseDO.setUnifiedSocialCreditCode(enterprisePO.getUnifiedSocialCreditCode());
            informationCorrelationEnterpriseService.save(informationCorrelationEnterpriseDO);
        }
        List<Long> chainIds = enterprisePO.getChainIds();
        if (CollectionUtil.isNotEmpty(chainIds)) {
            List<InformationCorrelationChainDO> list = CollectionUtil.newArrayList();
            chainIds.forEach(c -> {
                InformationCorrelationChainDO informationIdAndChainId = informationCorrelationChainService.getByInformationIdAndChainId(informationDO.getId(), c);
                if (Objects.isNull(informationIdAndChainId)) {
                    InformationCorrelationChainDO informationCorrelationChainDO = new InformationCorrelationChainDO();
                    informationCorrelationChainDO.setChainId(c);
                    informationCorrelationChainDO.setInformationId(informationDO.getId());
                    list.add(informationCorrelationChainDO);
                }
            });
            if (CollectionUtil.isNotEmpty(list)) {
                informationCorrelationChainService.saveBatch(list);
            }
        }
        informationDO.setIsEnterprise(Boolean.TRUE);
        informationDO.setIsInvestment(Boolean.TRUE);
        informationService.updateById(informationDO);
        informationService.syncToEsById(informationDO.getId());
    }

}
