package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.idicc.common.model.BaseDO;
import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.entity.data.*;
import cn.idicc.taotie.infrastructment.entity.xiaoai.*;
import cn.idicc.taotie.infrastructment.enums.MyCollectDataTypeEnum;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.data.EnterpriseMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.*;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.po.data.InvestmentProportionAmount;
import cn.idicc.taotie.infrastructment.po.data.UserCollectInfo;
import cn.idicc.taotie.infrastructment.response.data.EnterpriseDTO;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import cn.idicc.taotie.service.collector.EnterpriseRelationEsChangeTaskCollector;
import cn.idicc.taotie.service.search.EnterpriseSearch;
import cn.idicc.taotie.service.services.data.knowledge.strategy.config.KnowledgeStrategyEnum;
import cn.idicc.taotie.service.services.data.knowledge.strategy.context.KnowledgeSyncContext;
import cn.idicc.taotie.service.services.data.pangu.*;
import com.alibaba.nacos.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, EnterpriseDO> implements EnterpriseService {
	@Autowired
	private EnterpriseMapper enterpriseMapper;

	@Autowired
	private EnterpriseCorrelationLabelService enterpriseCorrelationLabelService;

	@Autowired
	private EnterpriseIndustryLabelRelationService enterpriseIndustryLabelRelationService;

	@Autowired
	private ExchangeRateService exchangeRateService;

	@Autowired
	private IndustryChainNodeEnterpriseScoreService industryChainNodeEnterpriseScoreService;

	@Autowired
	EnterpriseInvestmentProportionService enterpriseInvestmentProportionService;

	@Autowired
	MyCollectDataService myCollectDataService;

	@Autowired
	private IndustryChainNodeLabelRelationService industryChainNodeLabelRelationService;


	@Autowired
	private IndustryChainNodeService industryChainNodeService;

	@Autowired
	private EnterpriseSearch enterpriseSearch;


	@Autowired
	private EnterpriseRelationEsChangeTaskCollector enterpriseRelationEsChangeTaskCollector;

	@Autowired
	private DwsEnterpriseOriginMapper dwsEnterpriseOriginMapper;



	@Autowired
	private EnterpriseDevelopmentIndexMapper enterpriseDevelopmentIndexMapper;

	@Autowired
	@Qualifier("executor-pangu-io")
	private ExecutorService             poolExecutorByIo;
	@Autowired
	private InstAlumniAssociationMapper instAlumniAssociationMapper;

	@Autowired
	private InstAlumniAssociationMemberMapper instAlumniAssociationMemberMapper;

	@Autowired
	private AcademiaBasicInfoMapper     academiaBasicInfoMapper;

	@Autowired
	private EnterpriseTalentRelationMapper enterpriseTalentRelationMapper;

	@Autowired
	private TalentEducationExperienceMapper talentEducationExperienceMapper;

	@Autowired
	private KnowledgeSyncContext knowledgeSyncContext;

	@Override
	public EnterpriseDTO getByUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
		EnterpriseDO enterpriseDO = enterpriseMapper.selectOne(Wrappers.lambdaQuery(new EnterpriseDO())
				.eq(EnterpriseDO::getDeleted, Boolean.FALSE)
				.eq(EnterpriseDO::getUnifiedSocialCreditCode, unifiedSocialCreditCode));
		if (Objects.nonNull(enterpriseDO)) {
			EnterpriseDTO adapt = EnterpriseDTO.adapt(enterpriseDO);
			return adapt;
		}
		return null;
	}

	@Override
	public void doSyncDataToEs(String uniCode) {
		EnterpriseDO enterpriseDO = enterpriseMapper.selectByUnicode(uniCode);
		if (enterpriseDO == null) {
			log.error("doSyncDataToEs fail，未找到该企业，企业UniCode为：{}", uniCode);
			throw new BizException("未找到该企业");
		} else {
			doSyncDataToEs(enterpriseDO);
		}
	}

	@Override
	public void doSyncDataToEs(Long enterpriseId) {
		EnterpriseDO enterpriseDO = enterpriseMapper.selectById(enterpriseId);
		if (enterpriseDO == null) {
			log.error("doSyncDataToEs fail，未找到该企业，企业Id为：{}", enterpriseId);
			throw new BizException("未找到该企业");
		} else {
			doSyncDataToEs(enterpriseDO);
		}
	}

	@Override
	public void doSyncDataToEs(EnterpriseDO enterpriseDO) {
		this.doSyncDataToEs(Collections.singletonList(enterpriseDO));
	}

	/**
	 * 同步数据到ES
	 *
	 * @param list 企业基本信息列表
	 */
	@Override
	public void doSyncDataToEs(List<EnterpriseDO> list) {
		if (CollectionUtil.isNotEmpty(list)) {
			List<Long> enterpriseIds = list.stream().map(EnterpriseDO::getId).collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(enterpriseIds)) {
				Map<Long, List<EnterpriseCorrelationLabelDO>>      enterpriseLabelByEnterpriseIdMap = enterpriseCorrelationLabelService.mapByEnterpriseIds(enterpriseIds);
				Map<Long, List<EnterpriseIndustryLabelRelationDO>> industryLabelByEnterpriseIdMap   = enterpriseIndustryLabelRelationService.mapByEnterpriseIds(enterpriseIds);
				Map<String, Double>                                exchangeRateMap                  = exchangeRateService.getExchangeRateMap();
				List<IndustryChainNodeEnterpriseScoreDO> enterpriseNodeScoreList = industryChainNodeEnterpriseScoreService.list(Wrappers.lambdaQuery(IndustryChainNodeEnterpriseScoreDO.class)
						.eq(IndustryChainNodeEnterpriseScoreDO::getDeleted, Boolean.FALSE)
						.in(IndustryChainNodeEnterpriseScoreDO::getEnterpriseId, enterpriseIds));
				Map<Long, List<IndustryChainNodeEnterpriseScoreDO>> enterpriseNodeScoreMap = CollectionUtil.isNotEmpty(enterpriseNodeScoreList) ? enterpriseNodeScoreList.stream().collect(Collectors.groupingBy(IndustryChainNodeEnterpriseScoreDO::getEnterpriseId)) : null;

				List<String> unifiedSocialCreditCodes = list.stream().map(EnterpriseDO::getUnifiedSocialCreditCode).collect(Collectors.toList());
				// 查询产业研发投入数
				List<EnterpriseInvestmentProportionDO>              enterpriseInvestmentProportionDOS   = enterpriseInvestmentProportionService.listByParams(unifiedSocialCreditCodes);
				Map<String, List<EnterpriseInvestmentProportionDO>> enterpriseInvestmentProportionDOMap = enterpriseInvestmentProportionDOS.stream().collect(Collectors.groupingBy(e -> e.getUnifiedSocialCreditCode()));

				// 查询我关注的企业
				List<MyCollectDataDO>            myCollectDataDOS = myCollectDataService.listByEnterpriseIds(enterpriseIds, MyCollectDataTypeEnum.FOCUS_ENTERPRISE.getCode());
				Map<Long, List<MyCollectDataDO>> myCollectDataMap = myCollectDataDOS.stream().collect(Collectors.groupingBy(e -> e.getBusId()));

				//籍贯
//				Map<String, List<String>> ancestorMap = enterpriseAncestorMapper.selectByUniCodes(unifiedSocialCreditCodes).stream()
//						.collect(Collectors.groupingBy(EnterpriseAncestorDO::getUniCode, Collectors.mapping(EnterpriseAncestorDO::getAncestorCode, Collectors.toList())));
				//学校
//                Map<String,List<EnterpriseAcademiaDO>> academiaMap = enterpriseAcademiaMapper.selectByUniCodes(unifiedSocialCreditCodes).stream()
//                        .collect(Collectors.groupingBy(EnterpriseAcademiaDO::getUniCode
////                                , Collectors.mapping(EnterpriseAcademiaDO::getAcademicCode, Collectors.toList())
//                        ));
				//指标
				Map<String, EnterpriseDevelopmentIndexDO> indexMap = enterpriseDevelopmentIndexMapper.selectByUniCodes(unifiedSocialCreditCodes).stream()
						.collect(Collectors.toMap(EnterpriseDevelopmentIndexDO::getUniCode, e -> e, (e1, e2) -> e2));

				// 处理数据
				List<EnterprisePO> collect = CollectionUtil.newArrayList();
				list.forEach(l -> {
					if (Objects.nonNull(l)) {
						try {
							EnterprisePO adapt = EnterprisePO.adapt(l);
							if ("-".equals(adapt.getMobile()) || StringUtils.isEmpty(adapt.getMobile())) {
								if (StringUtils.isNotEmpty(adapt.getMoreMobile())) {
									adapt.setMobile(adapt.getMoreMobile());
								}
							}
							//设置注册日期
							String registerDate = l.getRegisterDate();
							transformationRegisterDate(registerDate, adapt);

							//设置注册资本转化单位为元后的值
							String registeredCapital = l.getRegisteredCapital();
							if (MapUtils.isNotEmpty(exchangeRateMap)) {
								transformationRegisteredCapital(registeredCapital, adapt, exchangeRateMap);
							}

							//设置关联企业标签id集合
							List<EnterpriseCorrelationLabelDO> enterpriseCorrelationLabelDOList = enterpriseLabelByEnterpriseIdMap.get(l.getId());
							if (CollectionUtil.isNotEmpty(enterpriseCorrelationLabelDOList)) {
								adapt.setEnterpriseLabelIds(enterpriseCorrelationLabelDOList.stream().map(EnterpriseCorrelationLabelDO::getLabelId).distinct().collect(Collectors.toList()));
							}

							//设置关联产业链标签id集合
							List<EnterpriseIndustryLabelRelationDO> enterpriseIndustryLabelRelationDOS = industryLabelByEnterpriseIdMap.get(l.getId());
							if (CollectionUtil.isNotEmpty(enterpriseIndustryLabelRelationDOS)) {
								List<Long> industryLabelIds = enterpriseIndustryLabelRelationDOS.stream().map(EnterpriseIndustryLabelRelationDO::getLabelId).distinct().collect(Collectors.toList());
								adapt.setIndustryLabelIds(industryLabelIds);
//								log.info("设置关联产业链标签id集合,enterprise-{}, {}, size:{}", l.getId(), industryLabelIds, enterpriseIndustryLabelRelationDOS.size());

								//设置关联产业链2级节点id集合和关联的产业链id集合
//								log.info("企业:{},labelIds :{}",l.getId(), industryLabelIds);
								List<IndustryChainNodeLabelRelationDO> industryChainNodeLabelRelationDOS = industryChainNodeLabelRelationService.listByLabelIds(industryLabelIds);
								if (CollectionUtil.isNotEmpty(industryChainNodeLabelRelationDOS)) {
									List<Long>                chainNodeIds            = industryChainNodeLabelRelationDOS.stream().map(IndustryChainNodeLabelRelationDO::getChainNodeId).distinct().collect(Collectors.toList());
									List<IndustryChainNodeDO> industryChainNodeDOList = industryChainNodeService.listByIds(chainNodeIds);
									if (CollectionUtil.isNotEmpty(industryChainNodeDOList)) {
										List<Long> chainIds = industryChainNodeDOList.stream().map(IndustryChainNodeDO::getChainId).distinct().collect(Collectors.toList());
										adapt.setChainIds(chainIds);
									}
									Set<IndustryChainNodeDO> parentNodeDOSet = CollectionUtil.newLinkedHashSet();
									for (IndustryChainNodeDO chainNode : industryChainNodeDOList) {
										List<IndustryChainNodeDO> tmpList = new ArrayList<>();
										industryChainNodeService.getParentNode(chainNode, tmpList);
										if (!tmpList.isEmpty()) {
											parentNodeDOSet.addAll(tmpList);
										}
									}
									if (CollectionUtil.isNotEmpty(parentNodeDOSet)) {
										Set<Long>                 secondNodeIdList   = parentNodeDOSet.stream().filter(e -> e.getNodeLevel().equals(2)).map(IndustryChainNodeDO::getId).collect(Collectors.toSet());
										Set<String>               secondNodeNameList = parentNodeDOSet.stream().filter(e -> e.getNodeLevel().equals(2)).map(IndustryChainNodeDO::getNodeName).collect(Collectors.toSet());
//										log.info("企业:{},二级节点 step1:{}",l.getId(), secondNodeIdList);
										List<IndustryChainNodeDO> nodeLevel2Special  = industryChainNodeDOList.stream().filter(e -> e.getNodeLevel().equals(2)).collect(Collectors.toList());
										if (!nodeLevel2Special.isEmpty()) {
											secondNodeIdList.addAll(nodeLevel2Special.stream().map(BaseDO::getId).collect(Collectors.toList()));
											secondNodeNameList.addAll(nodeLevel2Special.stream().map(IndustryChainNodeDO::getNodeName).collect(Collectors.toList()));
										}
//										log.info("企业:{},二级节点:{}", l.getId(), secondNodeNameList);
										adapt.setSecondChainNodeIds(new ArrayList<>(secondNodeIdList));
										adapt.setSecondChainNodeNames(new ArrayList<>(secondNodeNameList));

										Set<Long> nodeIds = parentNodeDOSet.stream().map(e -> e.getId()).collect(Collectors.toSet());
										nodeIds.addAll(chainNodeIds);
										adapt.setNodeIds(new ArrayList<>(nodeIds));
									}
								}
							}

							//设置当前企业的节点评分
							if (CollectionUtil.isNotEmpty(enterpriseNodeScoreMap)) {
								List<IndustryChainNodeEnterpriseScoreDO> nodeScoreList = enterpriseNodeScoreMap.get(l.getId());
								if (CollectionUtil.isNotEmpty(nodeScoreList)) {
									JSONObject jsonObject = new JSONObject();
									nodeScoreList.forEach(node -> jsonObject.set(node.getNodeId().toString(), node.getEnterpriseScore()));
									adapt.setNodeScore(jsonObject);
								}
							}

							// 设置产业研发投入
							if (MapUtils.isNotEmpty(enterpriseInvestmentProportionDOMap)) {
								List<EnterpriseInvestmentProportionDO> investmentProportionDOS = enterpriseInvestmentProportionDOMap.get(l.getUnifiedSocialCreditCode());
								if (CollectionUtil.isNotEmpty(investmentProportionDOS)) {
									List<InvestmentProportionAmount> investmentProportionAmounts = Lists.newArrayList();
									investmentProportionDOS.forEach(e -> investmentProportionAmounts.add(new InvestmentProportionAmount(e.getYear(), e.getAmount().doubleValue())));
									adapt.setInvestmentProportionAmounts(investmentProportionAmounts);
								}
							}

							// 设置我关注的企业
							if (MapUtils.isNotEmpty(myCollectDataMap)) {
								List<MyCollectDataDO> myCollectDataDOList = myCollectDataMap.get(l.getId());
								if (CollectionUtil.isNotEmpty(myCollectDataDOList)) {
									log.info("doSyncDataToEs,enterpriseId is:{}, myCollectDataDOList size is:{}", l.getId(), myCollectDataDOList.size());
									List<UserCollectInfo> userCollectInfo = Lists.newArrayList();
									myCollectDataDOList.forEach(e -> {
										Long collDate = e.getGmtCreate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
										userCollectInfo.add(new UserCollectInfo(e.getUserId(), collDate));
									});
									log.info("doSyncDataToEs,enterpriseId is:{}, userCollectInfo size is:{}", l.getId(), userCollectInfo.size());
									adapt.setUserCollectInfo(userCollectInfo);
								}
							}

							//籍贯
							List<DwsEnterpriseOriginDO> enterpriseOriginDOS = dwsEnterpriseOriginMapper.selectList(
									Wrappers.lambdaQuery(DwsEnterpriseOriginDO.class)
											.eq(DwsEnterpriseOriginDO::getUniCode, l.getUnifiedSocialCreditCode())
							);
							log.info("enterpriseId is:{}, enterpriseOriginDOS size is:{}", l.getId(), enterpriseOriginDOS.size());
							if (enterpriseOriginDOS != null && !enterpriseOriginDOS.isEmpty()) {
								List<String> codes = enterpriseOriginDOS.stream().map(DwsEnterpriseOriginDO::getRegionCode).collect(Collectors.toList());
								if (CollectionUtil.isNotEmpty(codes)) {
									Set<String> tmpSet = new HashSet<>();
									codes.forEach(code -> {
										tmpSet.add(code);
										tmpSet.add(code.substring(0, 2) + "0000");
										tmpSet.add(code.substring(0, 4) + "00");
									});
									adapt.setAncestorCode(new ArrayList<>(tmpSet));
								}
							}

							//学校
//                            if(MapUtils.isNotEmpty(academiaMap)){
//                                List<EnterpriseAcademiaDO> academiaDOS = academiaMap.get(l.getUnifiedSocialCreditCode());
//                                if (CollectionUtil.isNotEmpty(academiaDOS)) {
//                                    Set<String> tmpSet = new HashSet<>();
//                                    academiaDOS.forEach(academiaDO->{
//                                        String code = academiaDO.getAcademicCode();
//                                        tmpSet.add(code);
//                                        tmpSet.add(code.substring(0,2)+"0000");
//                                        tmpSet.add(code.substring(0,4)+"00");
//                                    });
//                                    adapt.setAcademicCode(new ArrayList<>(tmpSet));
//                                    adapt.setAcademicMd5s(academiaDOS.stream().map(EnterpriseAcademiaDO::getAcademicMd5).distinct().collect(Collectors.toList()));
//                                }
//                            }
							// 获取企业关联的人才，获取人才的教育经历，写入ES中
							Set<String> allAcademiaMd5 = new HashSet<>();
							List<AcademiaBasicInfoDO> allAcademia = new ArrayList<>();

							List<EnterpriseTalentRelationDO> enterpriseTalentRelationDOS =  enterpriseTalentRelationMapper.selectList(
									Wrappers.lambdaQuery(EnterpriseTalentRelationDO.class)
											.eq(EnterpriseTalentRelationDO::getUniCode, l.getUnifiedSocialCreditCode())
							);
							log.info("enterpriseId is:{}, enterpriseTalentRelationDOS size is:{}", l.getId(), enterpriseTalentRelationDOS.size());
							if (!enterpriseTalentRelationDOS.isEmpty()) {
								//获取所有人才的教育经历
								List<String> talentMd5s = enterpriseTalentRelationDOS.stream().map(EnterpriseTalentRelationDO::getTalentMd5).collect(Collectors.toList());
								List<TalentEducationExperienceDO> talentEducationExperienceDOS = talentEducationExperienceMapper.selectList(
										Wrappers.lambdaQuery(TalentEducationExperienceDO.class)
												.in(TalentEducationExperienceDO::getTalentMd5, talentMd5s)
								);
								if(!talentEducationExperienceDOS.isEmpty()){
									//获取所有的学校信息
									allAcademiaMd5 = talentEducationExperienceDOS.stream().map(TalentEducationExperienceDO::getAcademiaMd5).collect(Collectors.toSet());
									allAcademia = academiaBasicInfoMapper.selectList(Wrappers.lambdaQuery(AcademiaBasicInfoDO.class)
											.in(AcademiaBasicInfoDO::getAcademiaMd5, allAcademiaMd5));
								}
							}
							// 从校友会获取校友关联关系，并与上面的allAcademia合并
							List<InstAlumniAssociationDO> instAlumniAssociationDOS = instAlumniAssociationMapper
									.getRefAcademicByUniCode(l.getUnifiedSocialCreditCode());
							if(!instAlumniAssociationDOS.isEmpty()){
								Set<String> academiaMd5FromInstAlumni = instAlumniAssociationDOS.stream().map(InstAlumniAssociationDO::getAcademiaMd5).collect(Collectors.toSet());
								allAcademiaMd5.addAll(academiaMd5FromInstAlumni);
								List<AcademiaBasicInfoDO> academiaBasicInfoFromInstAlumni =
										academiaBasicInfoMapper.selectList(Wrappers.<AcademiaBasicInfoDO>lambdaQuery()
												.in(AcademiaBasicInfoDO::getAcademiaMd5,academiaMd5FromInstAlumni));
								allAcademia.addAll(academiaBasicInfoFromInstAlumni);
							}
							// 合并校友信息，写入ES
							if (!allAcademia.isEmpty()) {
								Set<String> regionCode = new HashSet<>();
								allAcademia.forEach(academia -> {
									String curRegionCode = academia.getAcademiaRegionCode();
									regionCode.add(curRegionCode);
									//省的code
									regionCode.add(curRegionCode.substring(0, 2) + "0000");
									//城市的code
									regionCode.add(curRegionCode.substring(0, 4) + "00");
								});
								adapt.setAcademicCode(new ArrayList<>(regionCode));
								adapt.setAcademicMd5s(Lists.newArrayList(allAcademiaMd5));
								//更新校友会有关的最后更新时间
								InstAlumniAssociationMemberDO instAlumniAssociationMemberDO = instAlumniAssociationMemberMapper.selectOne(
										Wrappers.lambdaQuery(InstAlumniAssociationMemberDO.class)
												.eq(InstAlumniAssociationMemberDO::getMemberUniCode, l.getUnifiedSocialCreditCode())
												.orderByDesc(InstAlumniAssociationMemberDO::getGmtCreate)
												.last("limit 1")
								);

								if(instAlumniAssociationMemberDO != null){
									ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(instAlumniAssociationMemberDO.getGmtCreate());
									adapt.setAlumniGmtModify(instAlumniAssociationMemberDO.getGmtCreate().toInstant(offset).toEpochMilli());
								}

							}




							//指标
							if (MapUtils.isNotEmpty(indexMap)) {
								EnterpriseDevelopmentIndexDO indexDO = indexMap.get(l.getUnifiedSocialCreditCode());
								if (indexDO != null) {
									adapt.setGrowthIndex(new Double(indexDO.getGrowthIndex().toString()));
									adapt.setExpansionIndex(new Double(indexDO.getExpansionIndex().toString()));
								}
							}
							collect.add(adapt);
						} catch (Exception e) {
							log.error(String.format("企业同步出现异常，传入企业参数为：[%s]", JSONUtil.toJsonStr(l)), e);
						}
					}
				});

				if (CollectionUtil.isNotEmpty(collect)) {
					enterpriseSearch.saveAll(collect);
					//同步到知识库ES
					knowledgeSyncContext.executeSync(KnowledgeStrategyEnum.ENTERPRISE_SYNC_STRATEGY, collect);
					CompletableFuture.runAsync(() -> {
						enterpriseRelationEsChangeTaskCollector.sendEnterpriseRelationEsChangeAdvice(collect);
					}, poolExecutorByIo);
				}
			}
		}
	}


	/**
	 * 根据企业名称批量查询企业信息
	 *
	 * @param enterpriseNames
	 * @return
	 */
	@Override
	public List<EnterpriseDO> queryByNames(List<String> enterpriseNames) {
		return enterpriseMapper.selectList(Wrappers.lambdaQuery(EnterpriseDO.class)
				.in(EnterpriseDO::getEnterpriseName, enterpriseNames));
	}

	/**
	 * 获取一条指定名称的企业信息
	 *
	 * @param enterpriseName
	 * @return
	 */
	@Override
	public EnterpriseDTO getByName(String enterpriseName) {
		EnterpriseDTO       result         = null;
		List<EnterpriseDTO> enterpriseDTOS = listByName(enterpriseName);
		if (CollectionUtil.isNotEmpty(enterpriseDTOS)) {
			result = enterpriseDTOS.get(0);
		}
		return result;
	}

	/**
	 * 获取指定名称的企业信息
	 *
	 * @param enterpriseName
	 * @return
	 */
	@Override
	public List<EnterpriseDTO> listByName(String enterpriseName) {
		List<EnterpriseDTO> result = CollectionUtil.newArrayList();
		List<EnterpriseDO> enterpriseDOS = enterpriseMapper.selectList(Wrappers.lambdaQuery(new EnterpriseDO())
				.eq(EnterpriseDO::getDeleted, Boolean.FALSE)
				.eq(EnterpriseDO::getEnterpriseName, enterpriseName));
		if (CollectionUtil.isNotEmpty(enterpriseDOS)) {
			result = enterpriseDOS.stream().map(EnterpriseDTO::adapt).collect(Collectors.toList());
		}
		return result;
	}

	private void transformationRegisterDate(String registerDate, EnterprisePO adapt) {
		Long registerDateStamp;
		if (StringUtils.isNotBlank(registerDate)) {
			registerDate = StringUtils.trim(registerDate);
			if (!StringUtils.equals(GlobalConstant.WHIPPLETREE, StringUtils.trim(registerDate))) {
				if (registerDate.length() == 10) {
					registerDateStamp = DateUtil.getTimestamp(DateUtil.asLocalDate(registerDate, DatePattern.NORM_DATE_PATTERN).atStartOfDay());
				} else {
					registerDateStamp = DateUtil.getTimestamp(DateUtil.Str2LocalDateTime(registerDate, DatePattern.NORM_DATETIME_PATTERN));
				}
				adapt.setRegisterDate(registerDateStamp);
			}
		}
	}

	private void transformationRegisteredCapital(String registeredCapital, EnterprisePO adapt, Map<String, Double> exchangeRateMap) {
		if (StringUtils.isNotBlank(registeredCapital) && !StringUtils.equals(GlobalConstant.WHIPPLETREE, registeredCapital)) {
			BigDecimal bigDecimal = industryChainNodeEnterpriseScoreService.currencyConverter(registeredCapital, exchangeRateMap);
			adapt.setRegisteredCapitalYuan(bigDecimal.toString());
		}
	}
}
