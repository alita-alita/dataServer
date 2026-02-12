package cn.idicc.taotie.service.kafka.consumer;

import cn.idicc.common.util.BeanUtil;
import cn.idicc.pangu.dto.AdministrativeDivisionDTO;
import cn.idicc.pangu.dto.EnterpriseDTO;
import cn.idicc.pangu.dto.EnterpriseIndustryLabelRelationDTO;
import cn.idicc.pangu.dto.InvestmentEnterpriseRecommendDTO;
import cn.idicc.pangu.service.AdministrativeDivisionRpcService;
import cn.idicc.pangu.service.EnterpriseIndustryLabelRpcService;
import cn.idicc.pangu.service.InvestmentEnterpriseRecommendRpcService;
import cn.idicc.taotie.infrastructment.constant.BusinessConstant;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseDO;
import cn.idicc.taotie.provider.api.service.EnterpriseSyncRpcService;
import cn.idicc.taotie.service.message.data.DataEntity;
import cn.idicc.taotie.service.message.data.KafkaDataMessage;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseService;
import cn.idicc.taotie.service.util.BusinessUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Objects;


/**
 * @Author: WangZi
 * @Date: 2023/8/17
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Component
public class Pangu_Bus_1_Consumer {

    @Autowired
    private BusinessUtil businessUtil;

    @Autowired
    private EnterpriseIndustryLabelSyncService enterpriseIndustryLabelSyncService;

    @DubboReference(interfaceClass = AdministrativeDivisionRpcService.class, check = false)
    private AdministrativeDivisionRpcService administrativeDivisionRpcService;

    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private EnterpriseSyncRpcService enterpriseSyncRpcService;

    @DubboReference(interfaceClass = InvestmentEnterpriseRecommendRpcService.class, check = false)
    private InvestmentEnterpriseRecommendRpcService investmentEnterpriseRecommendRpcService;

    @DubboReference(interfaceClass = EnterpriseIndustryLabelRpcService.class, check = false)
    private EnterpriseIndustryLabelRpcService enterpriseIndustryLabelRpcService;

    @Autowired
    private IncrementalDataSyncExecutor incrementalDataSyncExecutor;

    @KafkaListener(topics = "${system.env}pangu_bus_1")
    public void consumer(String msg, Acknowledgment ack) {
        log.info("Pangu_Bus_1_Consumer -> consumer request is:{}", msg);
        try {
            KafkaDataMessage message = JSONObject.parseObject(msg, KafkaDataMessage.class);
            String business_code = message.getBusiness_code();
            String action = message.getAction();
            DataEntity data = message.getData();
            String batch = message.getBatch();
            if (!businessUtil.getBatchLatest(batch)) {
                return;
            }
            try {
                Map<String, String> element = data.getElement();
                Long                key_id  = data.getKey_id();
                switch (business_code) {
                    case BusinessConstant.ADMINISTRATIVE_DIVISION:
                        //执行消费行政区划消费逻辑
                        switch (action) {
                            case BusinessConstant.INSERT:
                                AdministrativeDivisionDTO paramAdd = createAdministrativeDivisionParam(element, key_id);
                                administrativeDivisionRpcService.add(paramAdd);
                                break;
                            case BusinessConstant.UPDATE:
                                AdministrativeDivisionDTO paramUpdate = createAdministrativeDivisionParam(element, key_id);
                                administrativeDivisionRpcService.update(paramUpdate);
                                break;
                            case BusinessConstant.DELETE:
                                AdministrativeDivisionDTO paramDelete = createAdministrativeDivisionParam(element, key_id);
                                if (Objects.nonNull(paramDelete.getId())) {
                                    administrativeDivisionRpcService.delete(paramDelete.getId());
                                }
                            default:
                                break;
                        }
                        break;
                    case BusinessConstant.ENTERPRISE:
                        //执行消费
                        EnterpriseDTO enterpriseDTO = createEnterpriseParam(element, key_id);
                        saveEnterprise(enterpriseDTO);
                        break;
                    case BusinessConstant.INVESTMENT_ENTERPRISE_RECOMMEND:
                        if (BusinessConstant.INSERT.equals(action)) {
                            InvestmentEnterpriseRecommendDTO paramAdd = createInvestmentEnterpriseRecommendDTO(element, key_id);
                            investmentEnterpriseRecommendRpcService.add(paramAdd);
                        }
                        break;
                    case BusinessConstant.ENTERPRISE_INDUSTRY_LABEL_RELATION:
                        //执行消费行政区划消费逻辑
                        switch (action) {
                            case BusinessConstant.INSERT:
                                enterpriseIndustryLabelSyncService.add(message);
//                            EnterpriseIndustryLabelRelationDTO paramAdd = createEnterpriseIndustryLabelRelationParam(element, key_id);
//                            enterpriseIndustryLabelRpcService.add(paramAdd);
                                break;
                            case BusinessConstant.UPDATE:
                                enterpriseIndustryLabelSyncService.update(message);
//                            EnterpriseIndustryLabelRelationDTO paramUpdate = createEnterpriseIndustryLabelRelationParam(element, key_id);
//                            enterpriseIndustryLabelRpcService.update(paramUpdate);
                                break;
                            case BusinessConstant.DELETE:
                                enterpriseIndustryLabelSyncService.delete(message);
//                            EnterpriseIndustryLabelRelationDTO paramDelete = createEnterpriseIndustryLabelRelationParam(element, key_id);
//                            if (Objects.nonNull(paramDelete.getId())) {
//                                enterpriseIndustryLabelRpcService.delete(paramDelete);
//                            }
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        incrementalDataSyncExecutor.execute(message);
                        break;
                }
            } catch (Exception ex){
                throw  ex;
            } finally {
                businessUtil.addBatchByRedis(message.getBatch());
            }

        } catch (Exception e) {
            log.error("Pangu_Bus_1_Consumer -> consumer is error:", e);
        } finally {
            ack.acknowledge();
        }
    }

    private InvestmentEnterpriseRecommendDTO createInvestmentEnterpriseRecommendDTO(Map<String, String> element, Long key_id) {
        InvestmentEnterpriseRecommendDTO param = new InvestmentEnterpriseRecommendDTO();
        param.setId(key_id);
        param.setEnterpriseId(Long.valueOf(element.get("enterprise_id")));
        param.setOrgId(Long.valueOf(element.get("org_id")));
        param.setRecommendationReasonDetail(element.get("recommendation_reason_detail"));
        String recommendedDateStr = element.get("recommended_date");
        long recommendedDateStamp = Long.parseLong(recommendedDateStr);
        Instant instant = Instant.ofEpochMilli(recommendedDateStamp);
        LocalDateTime recommendedDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        param.setRecommendedDate(recommendedDate);
        param.setBatchNumber(element.get("batch_number"));
        param.setType(Integer.valueOf(element.get("type")));
        param.setRelationUserName(element.get("relation_user_name"));
        param.setRelationUserNameOnlyLogo(element.get("relation_user_name_only_logo"));
        param.setAssociationRelationship(element.get("association_relationship"));
        param.setResourceNeeds(element.get("resource_needs"));
        param.setAssociateLocalEnterpriseCode(element.get("associate_local_enterprise_code"));
        param.setAssociateLocalEnterpriseName(element.get("associate_local_enterprise_name"));
        param.setSupplyRelation(element.get("supply_relation"));
        param.setAssociatePolicy(element.get("associate_policy"));
        param.setAssociateInformationUrl(element.get("associate_information_url"));
        param.setOutsideInvestSatisfaction(element.get("outside_invest_satisfaction"));
        return param;
    }

    private EnterpriseDTO createEnterpriseParam(Map<String, String> element, Long key_id) {
        EnterpriseDTO param = new EnterpriseDTO();
        param.setId(key_id);
        param.setRegisterStatus(element.get("register_status"));
        param.setEnterpriseName(element.get("enterprise_name"));
        param.setEnglishName(element.get("english_name"));
        param.setUnifiedSocialCreditCode(element.get("unified_social_credit_code"));
        param.setTaxpayerIdentificationNumber(element.get("taxpayer_identification_number"));
        param.setRegistrationNumber(element.get("registration_number"));
        param.setOrganizeCode(element.get("organize_code"));
        param.setRegisteredCapital(element.get("registered_capital"));
        param.setPaidUpCapital(element.get("paid_up_capital"));
        param.setInsuredPersonsNumber(element.get("insured_persons_number"));
        param.setBusinessScope(element.get("business_scope"));
        param.setLegalPerson(element.get("legal_person"));
        param.setRegisterDate(element.get("register_date"));
        param.setApproveDate(element.get("approve_date"));
        param.setProvince(element.get("province"));
        param.setCity(element.get("city"));
        param.setArea(element.get("area"));
        param.setEnterpriseAddress(element.get("enterprise_address"));
        param.setEnterpriseType(element.get("enterprise_type"));
        param.setMobile(element.get("mobile"));
        param.setMoreMobile(element.get("more_mobile"));
        param.setEmail(element.get("email"));
        param.setMoreEmail(element.get("more_email"));
        param.setWebsite(element.get("website"));
        param.setNationalStandardIndustry(element.get("national_standard_industry"));

        String national_standard_industry_id = element.get("national_standard_industry_id");
        if (StringUtils.isNotBlank(national_standard_industry_id)) {
            param.setNationalStandardIndustryId(Long.valueOf(national_standard_industry_id));
        }

        param.setNationalStandardIndustryBig(element.get("national_standard_industry_big"));
        String national_standard_industry_big_id = element.get("national_standard_industry_big_id");
        if (StringUtils.isNotBlank(national_standard_industry_big_id)) {
            param.setNationalStandardIndustryBigId(Long.valueOf(national_standard_industry_big_id));
        }

        param.setNationalStandardIndustryMiddle(element.get("national_standard_industry_middle"));
        String national_standard_industry_middle_id = element.get("national_standard_industry_middle_id");
        if (StringUtils.isNotBlank(national_standard_industry_middle_id)) {
            param.setNationalStandardIndustryMiddleId(Long.valueOf(national_standard_industry_middle_id));
        }

        param.setNationalStandardIndustrySmall(element.get("national_standard_industry_small"));
        String national_standard_industry_small_id = element.get("national_standard_industry_small_id");
        if (StringUtils.isNotBlank(national_standard_industry_small_id)) {
            param.setNationalStandardIndustrySmallId(Long.valueOf(national_standard_industry_small_id));
        }
        param.setOperatingStatus(element.get("register_status"));
        param.setScale(element.get("scale"));
        param.setIntroduction(element.get("introduction"));
        param.setData360(element.get("data360"));
        return param;
    }

    private AdministrativeDivisionDTO createAdministrativeDivisionParam(Map<String, String> element, Long key_id) {
        AdministrativeDivisionDTO param = new AdministrativeDivisionDTO();
        param.setId(key_id);
        param.setProvince(element.get("province"));
        param.setCity(element.get("city"));
        param.setArea(element.get("area"));
        param.setCode(element.get("code"));
        param.setParentId(element.get("parent_id"));
        return param;
    }

    private EnterpriseIndustryLabelRelationDTO createEnterpriseIndustryLabelRelationParam(Map<String, String> element, Long key_id) {
        EnterpriseIndustryLabelRelationDTO param = new EnterpriseIndustryLabelRelationDTO();
        param.setId(key_id);
        if (element.containsKey("enterprise_id")) {
            param.setEnterpriseId(Long.valueOf(element.get("enterprise_id").split("\\.")[0]));
        }

        if (element.containsKey("label_id")) {
            param.setLabelId(Long.valueOf(element.get("label_id").split("\\.")[0]));
        }

        if (element.containsKey("batch_number")) {
            param.setBatchNumber(element.get("batch_number"));
        }

        if (element.containsKey("confirm")) {
            param.setConfirm(Boolean.valueOf(element.get("confirm")));
        }

        if (element.containsKey("hit_rule")) {
            param.setHitRule(Integer.valueOf(element.get("hit_rule").split("\\.")[0]));
        }
        return param;
    }

    /**
     * 2025-06-09，调用pangu的逻辑移动到taotie中进行，不再另外发起rpc调用，以便减少系统复杂度。
     * 本方法中，合并了新增和修改的逻辑，优先执行更新逻辑。
     *
     * @param param
     */
    private void saveEnterprise(EnterpriseDTO param) {
        EnterpriseDO enterpriseDO = BeanUtil.copyProperties(param, EnterpriseDO.class);
        String unifiedSocialCreditCode = enterpriseDO.getUnifiedSocialCreditCode();
//        try {
//            if (Objects.nonNull(enterpriseDO.getId())) {
//                // 优先执行更新逻辑，如是已存在数据更新后直接截断运行
//                EnterpriseDO enterpriseDTO = enterpriseService.getById(enterpriseDO.getId());
//                if (Objects.nonNull(enterpriseDTO)) {
//                    enterpriseSyncRpcService.updateByUnicode(enterpriseDO.getUnifiedSocialCreditCode());
//                    return;
//                }
//            }
//        } catch (Exception e) {
//            log.error("ads更新操作数据同步出现异常，传入参数为:[{}]", JSONUtil.toJsonStr(param), e);
//        }

        try {
            if (StringUtils.isNotBlank(unifiedSocialCreditCode)) {
                cn.idicc.taotie.infrastructment.response.data.EnterpriseDTO result = enterpriseService.getByUnifiedSocialCreditCode(unifiedSocialCreditCode);
                if (Objects.isNull(result)) {
                    enterpriseService.save(enterpriseDO);
                } else {
                    enterpriseDO.setId(result.getId());
                    enterpriseService.updateById(enterpriseDO);
                }
                enterpriseSyncRpcService.updateByUnicode(enterpriseDO.getUnifiedSocialCreditCode());
            }
        } catch (Exception e) {
            log.error("ads新增操作数据同步出现异常，企业社会统一信用代码为:{}", unifiedSocialCreditCode, e);
        }

    }

    public static void main(String[] args) {
        System.out.println(Boolean.valueOf("0.0"));
    }
}
