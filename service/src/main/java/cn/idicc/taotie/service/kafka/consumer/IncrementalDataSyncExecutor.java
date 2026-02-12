package cn.idicc.taotie.service.kafka.consumer;

import cn.idicc.common.util.SpringContextsUtil;
import cn.idicc.taotie.infrastructment.constant.BusinessConstant;
import cn.idicc.taotie.service.message.data.KafkaDataMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class IncrementalDataSyncExecutor {

	private static final Logger logger = LoggerFactory.getLogger(IncrementalDataSyncExecutor.class);

	@Autowired
	private ApplicationContext context;

	private static final Set<String> allowedPanGuMappers  = new HashSet<>();
	private static final Set<String> allowedXiaoAIMappers = new HashSet<>();

	static {
		allowedXiaoAIMappers.add("academiaBasicInfoMapper");
		allowedXiaoAIMappers.add("academiaChainRelationMapper");
		allowedXiaoAIMappers.add("cooperationMapper");
		allowedXiaoAIMappers.add("enterpriseAbnormalOperationMapper");
		allowedXiaoAIMappers.add("enterpriseAdminPenaltyMapper");
		allowedXiaoAIMappers.add("enterpriseCompetitiveProductMapper");
		allowedXiaoAIMappers.add("enterpriseCorrelationLabelFinancingMapper");
		allowedXiaoAIMappers.add("enterpriseCorrelationLabelListedMapper");
		allowedXiaoAIMappers.add("enterpriseCorrelationLabelTechMapper");
		allowedXiaoAIMappers.add("enterpriseDevelopmentIndexMapper");
		allowedXiaoAIMappers.add("enterpriseEnvironmentalPenaltyMapper");
		allowedXiaoAIMappers.add("enterpriseEventOverviewMapper");
		allowedXiaoAIMappers.add("enterpriseExecutedMapper");
		allowedXiaoAIMappers.add("enterpriseFinancingRecordMapper");
		allowedXiaoAIMappers.add("enterpriseIndustryChainMasterMapper");
		allowedXiaoAIMappers.add("enterpriseJudicialCaseMapper");
		allowedXiaoAIMappers.add("enterpriseMainStaffMapper");
		allowedXiaoAIMappers.add("enterpriseOutwardInvestmentMapper");
		allowedXiaoAIMappers.add("enterprisePartnerRelationshipMapper");
		allowedXiaoAIMappers.add("enterpriseProductMapper");
		allowedXiaoAIMappers.add("enterpriseQualificationMapper");
		allowedXiaoAIMappers.add("enterpriseRankingMapper");
		allowedXiaoAIMappers.add("enterpriseShareholderMapper");
		allowedXiaoAIMappers.add("enterpriseStockInfoMapper");
		allowedXiaoAIMappers.add("enterpriseTalentRelationMapper");
		allowedXiaoAIMappers.add("enterpriseTaxAbnormalMapper");
		allowedXiaoAIMappers.add("policyMapper");
		allowedXiaoAIMappers.add("policyIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("productIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("instAlumniAssociationMapper");
		allowedXiaoAIMappers.add("instAlumniAssociationMemberMapper");
		allowedXiaoAIMappers.add("instCommerceAssociationMapper");
		allowedXiaoAIMappers.add("instCommerceAssociationMemberMapper");
		allowedXiaoAIMappers.add("instGovOrganizationMapper");
		allowedXiaoAIMappers.add("instIndustryAssociationMapper");
		allowedXiaoAIMappers.add("instResearchInstitutionMapper");
		allowedXiaoAIMappers.add("institutionIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("ipPatentMapper");
		allowedXiaoAIMappers.add("ipPatentIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("newsMapper");
		allowedXiaoAIMappers.add("overseasAdminRegionMapper");
		allowedXiaoAIMappers.add("overseasAreaCountryMapper");
		allowedXiaoAIMappers.add("overseasEnterpriseMapper");
		allowedXiaoAIMappers.add("overseasEnterpriseIndustryChainKeyEnterpriseMapper");
		allowedXiaoAIMappers.add("overseasEnterpriseIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("overseasEnterpriseStockMapper");
		allowedXiaoAIMappers.add("overseasEventMapper");
		allowedXiaoAIMappers.add("overseasIndustryChainRegionIndexMapper");
		allowedXiaoAIMappers.add("overseasIndustryParkMapper");
		allowedXiaoAIMappers.add("overseasIndustryParkEnterpriseRelationMapper");
		allowedXiaoAIMappers.add("overseasIndustryParkIndustryChainIndexMapper");
		allowedXiaoAIMappers.add("overseasIndustryParkIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("overseasIndustryParkProfileMapper");
		allowedXiaoAIMappers.add("overseasIndustryParkRegionMapper");
		allowedXiaoAIMappers.add("overseasPolicyMapper");
		allowedXiaoAIMappers.add("overseasPolicyLabelMapper");
		allowedXiaoAIMappers.add("overseasProductMapper");
		allowedXiaoAIMappers.add("overseasProductIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("policyCorrelationLabelMapper");
		allowedXiaoAIMappers.add("projectMapper");
		allowedXiaoAIMappers.add("projectBiddingMapper");
		allowedXiaoAIMappers.add("projectIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("talentMapper");
		allowedXiaoAIMappers.add("talentEducationExperienceMapper");
		allowedXiaoAIMappers.add("talentIndustryChainRelationMapper");
		allowedXiaoAIMappers.add("talentQualificationMapper");
		allowedXiaoAIMappers.add("dwsEnterpriseOriginMapper");
		allowedXiaoAIMappers.add("industryEnterpriseChainNodeScoreMapper");
	}


	public void execute(KafkaDataMessage message) {
		logger.info("message:{}", message);
		if(!message.getData().getElement().containsKey("sourceCode")){
			return;
		}
		String sourceCode = message.getData().getElement().get("sourceCode");
		switch (sourceCode) {
			case "pangu":
				processDataBySource(message, "盘古");
				break;
			case "xiaoai":
				processDataBySource(message, "小AI");
				break;
			default:
				logger.info("未知的数据源:{}", sourceCode);
		}
	}

	private void processDataBySource(KafkaDataMessage message, String sourceName) {
		String action = message.getAction();
		if (BusinessConstant.DELETE.equals(action)) {
			processDeleteAction(message, sourceName);
		} else {
			logger.info("{}库暂不支持该操作:{}", sourceName, action);
		}
	}

	private void processDeleteAction(KafkaDataMessage message, String sourceName) {
		BaseMapper<?> mapper = getMapperBySource(message.getBusiness_code(), sourceName);
		if (mapper != null && message.getData().getElement().get("id") != null) {
			int effectRow = mapper.deleteById(message.getData().getElement().get("id"));
			logger.info("{}库删除操作影响行数:{},{}", sourceName, message.getData().getElement().get("id"), effectRow);
		} else if (mapper != null && message.getData().getElement().get("id") == null) {
			logger.info("{}库删除操作缺少主键id", sourceName);
		} else {
			logger.info("{}库暂不支持该操作", sourceName);
		}
	}

	private BaseMapper<?> getMapperBySource(String businessCode, String sourceName) {
		switch (sourceName) {
			case "盘古":
				if (allowedPanGuMappers.contains(businessCode)) {
					return (BaseMapper<?>) context.getBean(businessCode);
				}
			case "小AI":
				if (allowedXiaoAIMappers.contains(businessCode)) {
					return (BaseMapper<?>) context.getBean(businessCode);
				}
			default:
				return null;
		}
	}


}
