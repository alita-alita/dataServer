package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.common.util.BeanUtil;
import cn.idicc.identity.response.InstitutionDTO;
import cn.idicc.identity.response.UserDTO;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentAttractionClueMapper;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentAttractionClueDO;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentEntrustTaskDO;
import cn.idicc.taotie.infrastructment.enums.ClueStateEnum;
import cn.idicc.taotie.service.external.InstitutionService;
import cn.idicc.taotie.service.external.UserService;
import cn.idicc.taotie.service.search.EnterpriseSearch;
import cn.idicc.taotie.service.search.InvestmentAttractionClueSearch;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.data.InvestmentAttractionCluePO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentAttractionClueAssignRecordService;
import cn.idicc.taotie.service.services.data.pangu.InvestmentAttractionClueFollowUpRecordService;
import cn.idicc.taotie.service.services.data.pangu.InvestmentAttractionClueService;
import cn.idicc.taotie.service.services.data.pangu.InvestmentEntrustTaskService;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: WangZi
 * @Date: 2023/2/22
 * @Description: 招商线索实现层
 * @version: 1.0
 */
@Slf4j
@Service
public class InvestmentAttractionClueServiceImpl extends ServiceImpl<InvestmentAttractionClueMapper, InvestmentAttractionClueDO> implements InvestmentAttractionClueService {

    @Autowired
    private InvestmentAttractionClueMapper investmentAttractionClueMapper;

    @Autowired
    private EnterpriseSearch enterpriseSearch;

    @Autowired
    InstitutionService institutionService;

    @Autowired
    private InvestmentAttractionClueAssignRecordService investmentAttractionClueAssignRecordService;


    @Autowired
    private InvestmentAttractionClueFollowUpRecordService investmentAttractionClueFollowUpRecordService;

    @Autowired
    private InvestmentAttractionClueSearch investmentAttractionClueSearch;

    @Autowired
    private InvestmentEntrustTaskService investmentEntrustTaskService;

    @Autowired
    private UserService userService;

    @Override
    public void doSyncToEs(Long id) {
        InvestmentAttractionClueDO param = investmentAttractionClueMapper.selectById(id);
        Assert.notNull(param, String.format("传入线索id:%s不存在对应的记录", id));
        InvestmentAttractionCluePO result = BeanUtil.copyProperties(param, InvestmentAttractionCluePO.class);
        Optional<EnterprisePO> optional = enterpriseSearch.findById(result.getEnterpriseId());
        Assert.isTrue(optional.isPresent(), String.format("企业es中不存在招商线索关联的企业id:%s对应的记录", result.getEnterpriseId()));
        EnterprisePO enterprisePO = optional.get();
        InstitutionDTO institution = institutionService.getById(result.getOrgId());
        Assert.notNull(institution, String.format("不存在招商线索关联的入驻机构id:%s对应的记录", result.getOrgId()));
        result.setEnterpriseName(enterprisePO.getEnterpriseName());
        result.setUnifiedSocialCreditCode(enterprisePO.getUnifiedSocialCreditCode());
        result.setProvince(enterprisePO.getProvince());
        result.setCity(enterprisePO.getCity());
        result.setArea(enterprisePO.getArea());
        result.setRegisterDate(enterprisePO.getRegisterDate());
        result.setOrgCode(institution.getOrgCode());
        result.setOrgName(institution.getOrgName());
        result.setChainIds(enterprisePO.getChainIds());
        result.setSecondChainNodeIds(enterprisePO.getSecondChainNodeIds());
        result.setSecondChainNodeNames(enterprisePO.getSecondChainNodeNames());
        result.setEnterpriseLabelIds(enterprisePO.getEnterpriseLabelIds());
        result.setIndustryLabelIds(enterprisePO.getIndustryLabelIds());

        LocalDateTime intentionDate = param.getIntentionDate();
        if (Objects.nonNull(intentionDate)) {
            result.setIntentionDate(DateUtil.getTimestamp(intentionDate));
        }
        LocalDateTime recommendDate = param.getRecommendDate();
        if (Objects.nonNull(recommendDate)) {
            result.setRecommendDate(DateUtil.getTimestamp(recommendDate));
        }
        LocalDateTime assignDate = param.getAssignDate();
        if (Objects.nonNull(assignDate)) {
            result.setAssignDate(DateUtil.getTimestamp(assignDate));
        }
        LocalDateTime visitDate = param.getVisitDate();
        if (Objects.nonNull(visitDate)) {
            result.setVisitDate(DateUtil.getTimestamp(visitDate));
        }
        result.setGmtCreate(DateUtil.getTimestamp(param.getGmtCreate()));
        result.setGmtModify(DateUtil.getTimestamp(param.getGmtModify()));
        Integer clueState = result.getClueState();
        if (ClueStateEnum.ASSIGNED.getCode().equals(clueState)) {
            //填写历史指派人和历史被指派人字段属性值
            Set<Long> hisAssignPersonIds = investmentAttractionClueAssignRecordService.getHisAssignPersonIds(result.getId());
            Set<Long> hisBeAssignPersonIds = investmentAttractionClueAssignRecordService.getHisBeAssignPersonIds(result.getId());
            if (CollectionUtil.isNotEmpty(hisAssignPersonIds)) {
                result.setHisAssignPersonIds(CollectionUtil.newArrayList(hisAssignPersonIds));
            }
            if (CollectionUtil.isNotEmpty(hisBeAssignPersonIds)) {
                result.setHisBeAssignPersonIds(CollectionUtil.newArrayList(hisBeAssignPersonIds));
            }
        }
        Boolean havaInvestmentIntention = result.getHavaInvestmentIntention();
        if (Objects.nonNull(havaInvestmentIntention)) {
            //填写历史跟进人字段值
            Set<Long> hisVisitPersonIdsByClueId = investmentAttractionClueFollowUpRecordService.getHisVisitPersonIdsByClueId(result.getId());
            if (CollectionUtil.isNotEmpty(hisVisitPersonIdsByClueId)) {
                result.setHisVisitPersonIds(CollectionUtil.newArrayList(hisVisitPersonIdsByClueId));
            }
        }
        if (result.getEntrustOrNot()) {
            InvestmentEntrustTaskDO entrustTask = investmentEntrustTaskService.getOne(Wrappers.lambdaQuery(InvestmentEntrustTaskDO.class)
                    .eq(InvestmentEntrustTaskDO::getDeleted, Boolean.FALSE)
                    .eq(InvestmentEntrustTaskDO::getClueId, result.getId()));
            if (Objects.nonNull(entrustTask)) {
                result.setEntrustDate(entrustTask.getEntrustTimeStamp());
                Long entrustPersonId = entrustTask.getEntrustPersonId();
                result.setEntrustPersonId(entrustPersonId);
                result.setEntrustDealState(entrustTask.getFollowUpStatus());
                if (Objects.nonNull(entrustPersonId)) {
                    UserDTO user = userService.queryByUserId(entrustPersonId);
                    if (Objects.nonNull(user)) {
                        result.setEntrustPerson(user.getRealName());
                    }
                }
            }
        }
        investmentAttractionClueSearch.save(result);
    }

}






