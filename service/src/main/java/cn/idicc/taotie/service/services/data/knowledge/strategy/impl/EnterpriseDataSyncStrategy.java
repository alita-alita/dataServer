package cn.idicc.taotie.service.services.data.knowledge.strategy.impl;

import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.dao.knowledge.KnowledgeEnterpriseRepository;
import cn.idicc.taotie.infrastructment.dto.ExcludeRecommendRelationDTO;
import cn.idicc.taotie.infrastructment.dto.RecommendRelationDTO;
import cn.idicc.taotie.infrastructment.dto.RelationshipDTO;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseDO;
import cn.idicc.taotie.infrastructment.entity.data.IndustryLabelDO;
import cn.idicc.taotie.infrastructment.entity.dw.DwsEnterpriseInvestmentPromotionDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.*;
import cn.idicc.taotie.infrastructment.enums.AdministrativeDivisionEnums;
import cn.idicc.taotie.infrastructment.enums.ProjectTypeEnum;
import cn.idicc.taotie.infrastructment.mapper.data.EnterpriseMapper;
import cn.idicc.taotie.infrastructment.mapper.data.IndustryLabelMapper;
import cn.idicc.taotie.infrastructment.mapper.dw.DwsEnterpriseInvestmentPromotionMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.*;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeEnterprisePO;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import cn.idicc.taotie.service.services.data.knowledge.strategy.KnowledgeDataSyncStrategy;
import cn.idicc.wenchang.base.enums.RelationshipTypeEnum;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author guyongliang
 * @date 2025/11/6
 */
@Slf4j
@Service
public class EnterpriseDataSyncStrategy implements KnowledgeDataSyncStrategy<EnterprisePO> {


	@Autowired
	private EnterpriseProductMapper             enterpriseProductMapper;
	@Autowired
	private ProjectMapper                       projectMapper;
	@Autowired
	private KnowledgeEnterpriseRepository       knowledgeEnterpriseRepository;
	@Resource
	private EnterprisePartnerRelationshipMapper enterprisePartnerRelationshipMapper;

	@Autowired
	private EnterpriseFinancingInvestmentOrgRelationMapper enterpriseFinancingInvestmentOrgRelationMapper;
	@Autowired
	private InstInvestmentOrganizationMapper               instInvestmentOrganizationMapper;

	@Autowired
	private InstIndustryAssociationMemberMapper instIndustryAssociationMemberMapper;

	@Autowired
	private InstIndustryAssociationMapper instIndustryAssociationMapper;

	@Autowired
	private AcademiaBasicInfoMapper academiaBasicInfoMapper;

	@Autowired
	private EnterpriseMapper enterpriseMapper;

	@Autowired
	private IndustryLabelMapper industryLabelMapper;

	@Autowired
	private AdministrativeDivisionMapper               administrativeDivisionMapper;

	@Autowired
	private DwsEnterpriseInvestmentPromotionMapper dwsEnterpriseInvestmentPromotionMapper;


	@Override
	public void dataSync(EnterprisePO enterprisePO) {
		//TODO 错误逻辑
//		try {
//			log.info("同步企业籍贯信息(知识库):{}", enterprisePO.getUnifiedSocialCreditCode());
//			//同步产品名称
//			List<String> productNameList = enterpriseProductMapper.selectProductNameByUniCode(Collections.singletonList(enterprisePO.getUnifiedSocialCreditCode()))
//					.stream()
//					.map(EnterpriseProductDO::getProductName)
//					.filter(StringUtils::isNotBlank)
//					.collect(Collectors.toList());
//			KnowledgeEnterprisePO knowledgeEnterprisePO = BeanUtil.copyProperties(enterprisePO, KnowledgeEnterprisePO.class);
//			if (!productNameList.isEmpty()) {
//				knowledgeEnterprisePO.setEnterpriseProducts(productNameList);
//			}
//			knowledgeEnterpriseRepository.saveAll(Collections.singletonList(knowledgeEnterprisePO));
//		} catch (Exception e) {
//			log.error("同步企业籍贯信息中(知识库)：{}", e.getMessage());
//		}

		this.dataSync(Lists.newArrayList(enterprisePO));

	}

	@Override
	public void dataSync(List<EnterprisePO> enterprisePOs) {

		//企业代码列表
		List<String> uniCodes = enterprisePOs.stream()
				.map(EnterprisePO::getUnifiedSocialCreditCode)
				.collect(Collectors.toList());
		//链式企业
		List<EnterprisePartnerRelationshipDO> enterprisePartnerRelationshipDOS = enterprisePartnerRelationshipMapper.selectList(Wrappers.<EnterprisePartnerRelationshipDO>lambdaQuery()
				.in(EnterprisePartnerRelationshipDO::getSelfUniCode, uniCodes));
		Map<String, List<EnterprisePartnerRelationshipDO>> partnerRelationMap = enterprisePartnerRelationshipDOS.stream()
				.collect(Collectors.groupingBy(EnterprisePartnerRelationshipDO::getSelfUniCode));
		//企业产品名称
		Map<String, List<String>> productNameMap = enterpriseProductMapper.selectProductNameByUniCode(uniCodes)
				.stream()
				.collect(Collectors.groupingBy(EnterpriseProductDO::getEnterpriseUniCode, Collectors.mapping(EnterpriseProductDO::getProductName, Collectors.toList())));

		//关联高校信息
		Set<String> allAcademicMd5s = enterprisePOs.stream()
				.map(EnterprisePO::getAcademicMd5s)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
		List<AcademiaBasicInfoDO> academiaBasicInfoDOS = new ArrayList<>();
		if(!allAcademicMd5s.isEmpty()){
			academiaBasicInfoDOS = academiaBasicInfoMapper.selectList(Wrappers.lambdaQuery(AcademiaBasicInfoDO.class)
					.in(AcademiaBasicInfoDO::getAcademiaMd5, allAcademicMd5s));
		}

		Map<String, AcademiaBasicInfoDO> academiaBasicInfoMap;
		if (!academiaBasicInfoDOS.isEmpty()) {
			academiaBasicInfoMap = academiaBasicInfoDOS.stream()
					.collect(Collectors.toMap(AcademiaBasicInfoDO::getAcademiaMd5, value -> value));

		} else {
			academiaBasicInfoMap = new HashMap<>();
		}

		//创业项目
		Map<String, List<ProjectDO>> projectMap = projectMapper.selectList(Wrappers.<ProjectDO>lambdaQuery()
						.eq(ProjectDO::getProjectLabel, ProjectTypeEnum.ENTREPRENEURSHIP.getCode())
						.ge(ProjectDO::getRoadshowDate, DateUtil.format(DateUtil.plusDays(new Date(), -1 * 365 * 2)))
						.in(ProjectDO::getEnterpriseUniCode, uniCodes)
				)
				.stream()
				.collect(Collectors.groupingBy(ProjectDO::getEnterpriseUniCode));


		//企业与投资机构关系
		List<EnterpriseFinancingInvestmentOrgRelationDO> enterpriseFinancingInvestmentOrgRelationDOs = enterpriseFinancingInvestmentOrgRelationMapper.selectList(
				Wrappers.lambdaQuery(EnterpriseFinancingInvestmentOrgRelationDO.class)
						.in(EnterpriseFinancingInvestmentOrgRelationDO::getEnterpriseUniCode, uniCodes)
		);

		Map<String, List<EnterpriseFinancingInvestmentOrgRelationDO>> enterpriseFinancingInvestmentOrgRelationMap;
		Map<String, InstInvestmentOrganizationDO>                     investmentOrganizationDOMap;
		if (!enterpriseFinancingInvestmentOrgRelationDOs.isEmpty()) {
			enterpriseFinancingInvestmentOrgRelationMap = enterpriseFinancingInvestmentOrgRelationDOs
					.stream()
					.collect(Collectors.groupingBy(EnterpriseFinancingInvestmentOrgRelationDO::getEnterpriseUniCode));

			Set<String> organizationUniCodes = enterpriseFinancingInvestmentOrgRelationMap.values().stream()
					.flatMap(Collection::stream)
					.map(EnterpriseFinancingInvestmentOrgRelationDO::getOrganizationUniCode)
					.collect(Collectors.toSet());
			//投资机构信息
			investmentOrganizationDOMap = instInvestmentOrganizationMapper.selectList(
					Wrappers.lambdaQuery(InstInvestmentOrganizationDO.class)
							.in(InstInvestmentOrganizationDO::getUniCode, organizationUniCodes)
			).stream().collect(Collectors.toMap(InstInvestmentOrganizationDO::getUniCode, value -> value));
		} else {
			enterpriseFinancingInvestmentOrgRelationMap = new HashMap<>();
			investmentOrganizationDOMap = new HashMap<>();
		}

		//关联的协会信息
		List<InstIndustryAssociationMemberDO> instIndustryAssociationMemberDOs = instIndustryAssociationMemberMapper.selectList(
				Wrappers.lambdaQuery(InstIndustryAssociationMemberDO.class)
						.in(InstIndustryAssociationMemberDO::getMemberUniCode, uniCodes)
		);
		Map<String, List<InstIndustryAssociationMemberDO>> enterpriseRefAssociationMap;
		Map<String, InstIndustryAssociationDO>             instIndustryAssociationDOMap;
		if (!instIndustryAssociationMemberDOs.isEmpty()) {
			enterpriseRefAssociationMap = instIndustryAssociationMemberDOs.stream()
					.collect(Collectors.groupingBy(InstIndustryAssociationMemberDO::getMemberUniCode));

			Set<String> associationMd5s = instIndustryAssociationMemberDOs.stream()
					.map(InstIndustryAssociationMemberDO::getIndustryAssociationId)
					.collect(Collectors.toSet());
			//协会基本信息
			instIndustryAssociationDOMap = instIndustryAssociationMapper.selectList(
					Wrappers.lambdaQuery(InstIndustryAssociationDO.class)
							.in(InstIndustryAssociationDO::getAssociationMd5, associationMd5s)
			).stream().collect(Collectors.toMap(InstIndustryAssociationDO::getAssociationMd5, value -> value));
		} else {
			enterpriseRefAssociationMap = new HashMap<>();
			instIndustryAssociationDOMap = new HashMap<>();
		}

		List<KnowledgeEnterprisePO> knowledgeEnterprisePOs = enterprisePOs.stream()
				.map(item -> {
					KnowledgeEnterprisePO knowledgeEnterprisePO = BeanUtil.copyProperties(item, KnowledgeEnterprisePO.class);
					//企业产品
					List<String> enterpriseProducts = productNameMap.get(item.getUnifiedSocialCreditCode());
					Set<String> enterpriseProductSet = new HashSet<>();
					if(enterpriseProducts != null) {
						enterpriseProductSet.addAll(enterpriseProducts);
					}
					if(item.getIndustryLabelIds()!= null && !item.getIndustryLabelIds().isEmpty()) {
						List<IndustryLabelDO> industryLabelDOs = industryLabelMapper.selectList(Wrappers.lambdaQuery(
								IndustryLabelDO.class
						).in(IndustryLabelDO::getId, item.getIndustryLabelIds()));

						Set<String> industryLabels = industryLabelDOs.stream()
								.map(IndustryLabelDO::getLabelName)
								.collect(Collectors.toSet());
						enterpriseProductSet.addAll(industryLabels);
					}
					if (CollectionUtils.isNotEmpty(enterpriseProductSet)) {
						knowledgeEnterprisePO.setEnterpriseProducts(Lists.newArrayList(enterpriseProductSet));
					}

					if(item.getAcademicMd5s() != null) {
						//学校信息
						Set<String> academicNames = item.getAcademicMd5s().stream().map(academiaBasicInfoMap::get)
								.map(AcademiaBasicInfoDO::getAcademiaName).collect(Collectors.toSet());

						knowledgeEnterprisePO.setReferenceAcademiaNames(academicNames);
						knowledgeEnterprisePO.setAcademicRegionCode(item.getAcademicCode());
					}


					//创业企业项目
					List<ProjectDO> projects = projectMap.get(item.getUnifiedSocialCreditCode());
					if (CollectionUtils.isNotEmpty(projects)) {
						knowledgeEnterprisePO.setProjectNames(projects.stream().map(ProjectDO::getProjectName).collect(Collectors.toList()));
					}

					//链式关系
					List<EnterprisePartnerRelationshipDO> partnerUniCodes = partnerRelationMap.get(item.getUnifiedSocialCreditCode());
					if (CollectionUtils.isNotEmpty(partnerUniCodes)) {
						knowledgeEnterprisePO.setPartnerUniCodes(partnerUniCodes.stream().map(EnterprisePartnerRelationshipDO::getCounterpartUniCode).collect(Collectors.toList()));
						List<RelationshipDTO> relationshipDTOList = new ArrayList<>();
						for (EnterprisePartnerRelationshipDO relationshipDO : partnerUniCodes) {
							RelationshipDTO relationshipDTO = new RelationshipDTO();
							EnterpriseDO    enterprise      = enterpriseMapper.selectByUnicode(relationshipDO.getCounterpartUniCode());
							if(enterprise == null){
								continue;
							}
							relationshipDTO.setCounterpartEnterpriseName(enterprise.getEnterpriseName());
							try {
								Integer relationshipType = relationshipDO.getRelationshipType();
								relationshipDTO.setHisRelationship(RelationshipTypeEnum.getByCode(relationshipType).getDesc());
							} catch (NumberFormatException e) {
								log.warn("Invalid relationship type: {}", relationshipDO.getRelationshipType());
							}
							if (StringUtils.isNotBlank(relationshipDO.getRelationshipDegree())) {
								relationshipDTO.setHisRelationshipDegree(relationshipDO.getRelationshipDegree());
							}
							relationshipDTOList.add(relationshipDTO);
						}
						if (CollectionUtils.isNotEmpty(relationshipDTOList)) {
							knowledgeEnterprisePO.setRelationships(relationshipDTOList);
						}
					}

					if (!enterpriseFinancingInvestmentOrgRelationDOs.isEmpty()) {
						//关联的投资机构信息
						List<String> investmentOrganizationUniCodes = enterpriseFinancingInvestmentOrgRelationMap.get(item.getUnifiedSocialCreditCode())
								.stream()
								.map(EnterpriseFinancingInvestmentOrgRelationDO::getOrganizationUniCode)
								.collect(Collectors.toList());

						if (CollectionUtils.isNotEmpty(investmentOrganizationUniCodes)) {
							//机构名称
							Set<String> investmentOrganizationNames = investmentOrganizationUniCodes.stream()
									.filter(investmentOrganizationDOMap::containsKey)
									.map(investmentOrganizationDOMap::get)
									.filter(Objects::nonNull)
									.map(InstInvestmentOrganizationDO::getOrganizationName)
									.collect(Collectors.toSet());
							knowledgeEnterprisePO.setReferenceInvestmentOrganization(investmentOrganizationNames);
						}
					}

					if (!instIndustryAssociationMemberDOs.isEmpty()) {
						//关联的协会信息
						Set<String> refAssociationIds = enterpriseRefAssociationMap.get(item.getUnifiedSocialCreditCode())
								.stream().map(InstIndustryAssociationMemberDO::getIndustryAssociationId)
								.collect(Collectors.toSet());
						Set<String> refAssociationNames = refAssociationIds.stream()
								.map(instIndustryAssociationDOMap::get)
								.filter(Objects::nonNull)
								.map(InstIndustryAssociationDO::getAssociationName)
								.collect(Collectors.toSet());
						if (CollectionUtils.isNotEmpty(refAssociationNames)) {
							knowledgeEnterprisePO.setReferenceAssociationNames(refAssociationNames);
						}
					}

					//乡贤关系
					knowledgeEnterprisePO.setAncestorCode(item.getAncestorCode());

					//推荐信息处理
					// 产业链 + 地区
					List<DwsEnterpriseInvestmentPromotionDO> recommends = dwsEnterpriseInvestmentPromotionMapper.selectList(
							Wrappers.lambdaQuery(DwsEnterpriseInvestmentPromotionDO.class)
									.select(DwsEnterpriseInvestmentPromotionDO::getIndustryChainId,
											DwsEnterpriseInvestmentPromotionDO::getRecommendedUniCode,
											DwsEnterpriseInvestmentPromotionDO::getExcludeRegionCode,
											DwsEnterpriseInvestmentPromotionDO::getRecommendRegionCode)
									.eq(DwsEnterpriseInvestmentPromotionDO::getRecommendedUniCode, item.getUnifiedSocialCreditCode())
									//只要校友、乡贤
									.in(DwsEnterpriseInvestmentPromotionDO::getClueType, 1, 2)
									 .eq(DwsEnterpriseInvestmentPromotionDO::getDeleted, 0)
					);
					if (CollectionUtils.isNotEmpty(recommends)) {
						List<DwsEnterpriseInvestmentPromotionDO> recommendRelationDTOS = recommends.stream().filter(row ->
								StringUtils.isNotEmpty(row.getRecommendRegionCode())).collect(Collectors.toList());
						Set<RecommendRelationDTO> rrs = recommendRelationDTOS.stream().map(row -> {
							RecommendRelationDTO recommendRelationDTO = new RecommendRelationDTO();
							recommendRelationDTO.setChainId(Long.valueOf(row.getIndustryChainId()));
							if(AdministrativeDivisionEnums.CHINA.getCode().equals(row.getRecommendRegionCode())){
								recommendRelationDTO.setProvince(AdministrativeDivisionEnums.CHINA.getName());
								recommendRelationDTO.setCity(AdministrativeDivisionEnums.CHINA.getName());
							} else {
								AdministrativeDivisionDO recommendRegion = administrativeDivisionMapper.selectOne(
										Wrappers.lambdaQuery(AdministrativeDivisionDO.class)
												.eq(AdministrativeDivisionDO::getCode, row.getRecommendRegionCode())
												.last("limit 1")
								);
								if (recommendRegion != null) {
									recommendRelationDTO.setProvince(recommendRegion.getProvince());
									recommendRelationDTO.setCity(recommendRegion.getCity());
								}
							}

							return recommendRelationDTO;
						}).collect(Collectors.toSet());
						if(!rrs.isEmpty()) {
							knowledgeEnterprisePO.setRecommendRelations(Lists.newArrayList(rrs));
						}
						//TODO 校友、乡贤 没有规避逻辑
//						List<DwsEnterpriseInvestmentPromotionDO> excludeRecommends = recommends.stream().filter(row ->
//								StringUtils.isNotEmpty(row.getExcludeRegionCode())).collect(Collectors.toList());
//
//						Set<ExcludeRecommendRelationDTO> ers = excludeRecommends.stream().map(row -> {
//							ExcludeRecommendRelationDTO excludeRecommendRelationDTO = new ExcludeRecommendRelationDTO();
//							excludeRecommendRelationDTO.setChainId(Long.valueOf(row.getIndustryChainId()));
//							AdministrativeDivisionDO excludeRegion = administrativeDivisionMapper.selectOne(
//									Wrappers.lambdaQuery(AdministrativeDivisionDO.class)
//											.eq(AdministrativeDivisionDO::getCode, row.getExcludeRegionCode())
//											.last("limit 1")
//							);
//							if (excludeRegion != null) {
//								excludeRecommendRelationDTO.setExcludeProvince(excludeRegion.getProvince());
//								excludeRecommendRelationDTO.setExcludeCity(excludeRegion.getCity());
//							}
//							return excludeRecommendRelationDTO;
//						}).collect(Collectors.toSet());
//						if(!ers.isEmpty()) {
//							knowledgeEnterprisePO.setExcludeRecommendRelations(Lists.newArrayList(ers));
//						}
					}

					knowledgeEnterprisePO.setEnterpriseSummary(item.getIntroduction());
					return knowledgeEnterprisePO;
				})
				.collect(Collectors.toList());
		knowledgeEnterpriseRepository.saveAll(knowledgeEnterprisePOs);
	}

}
