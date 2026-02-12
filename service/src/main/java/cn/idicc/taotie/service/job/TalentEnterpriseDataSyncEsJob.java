package cn.idicc.taotie.service.job;

import cn.hutool.core.date.DatePattern;
import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseDO;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseLabelDO;
import cn.idicc.taotie.infrastructment.entity.data.ExchangeRateDO;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.AdministrativeDivisionDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.EnterpriseTalentRelationDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.TalentDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.TalentQualificationDO;
import cn.idicc.taotie.infrastructment.enums.AmountUnitEnum;
import cn.idicc.taotie.infrastructment.mapper.data.EnterpriseMapper;
import cn.idicc.taotie.infrastructment.mapper.data.ExchangeRateMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.*;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeTalentEnterprisePO;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import cn.idicc.taotie.service.services.data.knowledge.strategy.config.KnowledgeStrategyEnum;
import cn.idicc.taotie.service.services.data.knowledge.strategy.context.KnowledgeSyncContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TalentEnterpriseDataSyncEsJob {

	/**
	 * 人名称，企业名称，企业地址，人员地址+额外查询条件
	 */
	@Autowired
	private TalentMapper talentMapper;

	@Autowired
	private TalentQualificationMapper talentQualificationMapper;

	@Autowired
	private ExchangeRateMapper exchangeRateMapper;

	private List<ExchangeRateDO> exchangeRateList;
	@Autowired
	private EnterpriseTalentRelationMapper enterpriseTalentRelationMapper;
	@Autowired
	private EnterpriseMapper enterpriseMapper;

	@Autowired
	private EnterpriseCorrelationLabelMapper enterpriseCorrelationLabelMapper;

	@Autowired
	private AdministrativeDivisionMapper administrativeDivisionMapper;

	@Autowired
	private TalentIndustryChainRelationMapper talentIndustryChainRelationMapper;

	@Autowired
	private KnowledgeSyncContext knowledgeSyncContext;

	@PostConstruct
	public void initExchangeRateList() {
		exchangeRateList = exchangeRateMapper.selectList(Wrappers.lambdaQuery(ExchangeRateDO.class));
	}

	public static final List<String> LEADING_TALENT = Arrays.asList("创新创业领军人才", "创新领军人才", "创业领军人才");

	@XxlJob("KnowledgeTalentEnterpriseDataSyncEs")
	public void doSyncEs() {
		//同步到知识库ES
		long pageSize = 1000;
		long start = 1;
		List<String> batchTalentMd5s;
		Page<TalentDO> talentDOPage;
		List<String> talentMd5s = new ArrayList<>();
		Page<TalentDO> page = new Page<>(start, 1000); // 初始页码为1，每页1000条
		while (true) {
			log.info("current page: {}, page size: {}", start, pageSize);
			LambdaQueryWrapper<TalentDO> lambdaQueryWrapper = Wrappers.lambdaQuery(TalentDO.class);
			talentDOPage = talentMapper.selectPage(page, lambdaQueryWrapper);
			// 空值检查避免NPE
			if (talentDOPage == null || CollectionUtils.isEmpty(talentDOPage.getRecords())) {
				break; // 没有更多数据时退出循环
			}
			talentMd5s.clear(); // 清空前一批次的数据
			for (TalentDO record : talentDOPage.getRecords()) {
				// 避免向集合中添加null值
				if (record != null && record.getTalentMd5() != null) {
					talentMd5s.add(record.getTalentMd5());
				}
			}
			batchTalentMd5s = talentQualificationMapper.getBytalentMd5(LEADING_TALENT, talentMd5s);
			if (CollectionUtils.isEmpty(batchTalentMd5s)) {
				break;
			}
			List<KnowledgeTalentEnterprisePO> finalTalentDOList = processTalentEnterpriseData(batchTalentMd5s);
//			knowledgeSyncContext.executeSync(KnowledgeStrategyEnum.TALENT_ENTERPRISE_SYNC_STRATEGY, finalTalentDOList);
			page.setCurrent(start++); // 更新页码
		}
	}


	private List<KnowledgeTalentEnterprisePO> processTalentEnterpriseData(List<String> batchTalentMd5s) {
		List<KnowledgeTalentEnterprisePO> records = new ArrayList<>();
		for (String talentMd5 : batchTalentMd5s) {
			//准备人才基本信息
			TalentDO talentDO = talentMapper.selectOne(Wrappers.<TalentDO>lambdaQuery()
					.eq(TalentDO::getTalentMd5, talentMd5));
			if (talentDO == null) {
				log.info("can not find talent by talentMd5 {}", talentMd5);
				continue;
			}
			//人才基本信息
			KnowledgeTalentEnterprisePO currentRow = new KnowledgeTalentEnterprisePO();
			currentRow.setId(talentDO.getId()
					.toString());
			currentRow.setTalentMd5(talentDO.getTalentMd5());
			currentRow.setTalentName(talentDO.getTalentName());
			if (!StringUtils.isEmpty(talentDO.getTalentAncestorHomeRegionCode())) {
				Set<String> regionCodes = new HashSet<>();
				regionCodes.add(talentDO.getTalentAncestorHomeRegionCode()
						.substring(0, 2) + "0000");
				regionCodes.add(talentDO.getTalentAncestorHomeRegionCode()
						.substring(0, 4) + "00");
				regionCodes.add(talentDO.getTalentAncestorHomeRegionCode());
				currentRow.setTalentAncestorCode(Lists.newArrayList(regionCodes));
			}
			AdministrativeDivisionDO administrativeDivisionDO = administrativeDivisionMapper.selectOne(
					Wrappers.lambdaQuery(AdministrativeDivisionDO.class)
							.eq(AdministrativeDivisionDO::getCode, talentDO.getTalentAncestorHomeRegionCode())
							.eq(AdministrativeDivisionDO::getDeleted, false)
							.last("limit 1")
			);
			if (administrativeDivisionDO != null) {
				currentRow.setTalentProvince(administrativeDivisionDO.getProvince());
				currentRow.setTalentCity(administrativeDivisionDO.getCity());
				currentRow.setTalentArea(administrativeDivisionDO.getArea());
			}
			currentRow.setResearchField(talentDO.getResearchField());

			//人才关联的企业信息
			EnterpriseTalentRelationDO enterpriseTalentRelationDO = enterpriseTalentRelationMapper.selectOne(
					Wrappers.lambdaQuery(EnterpriseTalentRelationDO.class)
							.eq(EnterpriseTalentRelationDO::getTalentMd5, talentDO.getTalentMd5())
							.last("limit 1")
			);
			if (enterpriseTalentRelationDO == null) {
				log.info("can not find enterpriseTalentRelationDO by talentMd5 {}", talentDO.getTalentMd5());
				continue;
			}
			currentRow.setOccupation(enterpriseTalentRelationDO.getOccupation());

			EnterpriseDO enterprise = enterpriseMapper.selectByUnicode(enterpriseTalentRelationDO.getUniCode());
			if (enterprise == null) {
				log.info("can not find enterprise by unicode {}", enterpriseTalentRelationDO.getUniCode());
				continue;
			}
			currentRow.setEnterpriseId(enterprise.getId()
					.toString());
			currentRow.setEnterpriseMobile(enterprise.getMobile());
			currentRow.setEnterpriseName(enterprise.getEnterpriseName());
			currentRow.setEnterpriseAddress(enterprise.getEnterpriseAddress());
			currentRow.setEnterpriseCity(enterprise.getCity());
			currentRow.setEnterpriseArea(enterprise.getArea());
			currentRow.setEnterpriseProvince(enterprise.getProvince());
			currentRow.setUniCode(enterprise.getUnifiedSocialCreditCode());
			currentRow.setEnterpriseScale(enterprise.getScale());

			currentRow.setEstablishment(convertDateStringToTimestamp(enterprise.getRegisterDate()));

			currentRow.setRegisteredCapital(currencyConverter(enterprise.getRegisteredCapital()).toString());

			//获取企业科技型标签
			List<EnterpriseLabelDO> enterpriseLabelDOS = enterpriseCorrelationLabelMapper.getLabelsByEnterpriseId(enterprise.getId(), 6);
			if (!enterpriseLabelDOS.isEmpty()) {
				List<Long> labelNames = enterpriseLabelDOS.stream()
						.map(EnterpriseLabelDO::getId)
						.collect(Collectors.toList());
				currentRow.setTechnologicalInnovation(labelNames);
			}
			List<EnterpriseLabelDO> listedLabels = enterpriseCorrelationLabelMapper.getLabelsByEnterpriseId(
					enterprise.getId(), 1);
			if (!listedLabels.isEmpty()) {
				currentRow.setListedSector(listedLabels.get(0)
						.getId()
						.toString());
			}
			List<EnterpriseLabelDO> financingLabels = enterpriseCorrelationLabelMapper.getLabelsByEnterpriseId(
					enterprise.getId(), 2);
			if (!financingLabels.isEmpty()) {
				currentRow.setFinancingRounds(financingLabels.get(0)
						.getId()
						.toString());
			}

			List<IndustryChainDO> chainDTOS = talentIndustryChainRelationMapper.selectByTalent(talentMd5);
			Set<Long> chainIds = chainDTOS.stream()
					.map(IndustryChainDO::getId)
					.collect(Collectors.toSet());
			currentRow.setChainIds(chainIds);

			TalentQualificationDO talentQualificationDO = talentQualificationMapper.selectOne(Wrappers.lambdaQuery(TalentQualificationDO.class)
					.eq(TalentQualificationDO::getTalentMd5, enterpriseTalentRelationDO.getTalentMd5())
					.last("limit 1"));

			/*			long modifyTimestamp = enterpriseTalentRelationDO.getGmtModify().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() * 1000;*/
			long createTimestamp = enterpriseTalentRelationDO.getGmtCreate()
					.atZone(ZoneId.systemDefault())
					.toInstant()
					.getEpochSecond() * 1000;
			currentRow.setModifyTime(createTimestamp);
			records.add(currentRow);
		}
		return records;
	}

	private long convertDateStringToTimestamp(String registerDate) {
		if (registerDate == null || registerDate.trim()
				.isEmpty()) {
			return 0L;
		}
		Long registerDateStamp;
		if (StringUtils.isNotBlank(registerDate)) {
			registerDate = StringUtils.trim(registerDate);
			if (!StringUtils.equals(GlobalConstant.WHIPPLETREE, StringUtils.trim(registerDate))) {
				if (registerDate.length() == 10) {
					registerDateStamp = DateUtil.getTimestamp(DateUtil.asLocalDate(registerDate, DatePattern.NORM_DATE_PATTERN)
							.atStartOfDay());
				} else {
					registerDateStamp = DateUtil.getTimestamp(DateUtil.Str2LocalDateTime(registerDate, DatePattern.NORM_DATETIME_PATTERN));
				}
				return registerDateStamp;
			}
		}
		return 0l;
	}


	private BigDecimal currencyConverter(String registeredCapital) {
		if (CollectionUtils.isEmpty(exchangeRateList)) {
			throw new RuntimeException("请配置换算汇率");
		}
		// 保留最新的汇率
		Map<String, BigDecimal> exchangeRateMap = exchangeRateList.stream()
				.collect(Collectors.toMap(
						ExchangeRateDO::getCurrency,
						item -> BigDecimal.valueOf(Optional.ofNullable(item.getExchangeRate())
								.orElse(0.0D)),
						(existing, replacement) -> replacement
				));
		BigDecimal registeredCapitalNew = null;
		BigDecimal tenThousand = new BigDecimal("10000");
		if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(GlobalConstant.UNIT)
				.concat(AmountUnitEnum.CNY.getDesc()))) {
			registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(GlobalConstant.UNIT)
					.concat(AmountUnitEnum.CNY.getDesc())))).multiply(tenThousand)
					.setScale(2, RoundingMode.DOWN);
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.USD.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.USD.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.USD.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.EUR.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.EUR.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.EUR.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.JPY.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.JPY.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.JPY.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.GBP.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.GBP.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.GBP.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.CHF.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.CHF.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.CHF.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.CAD.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.CAD.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.CAD.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.HKD.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.HKD.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.HKD.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.AUD.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.AUD2.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.AUD.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.SGD.getDesc()))) {
			BigDecimal exchangeRate = exchangeRateMap.get(AmountUnitEnum.SGD.getDesc());
			if (exchangeRate != null) {
				registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(AmountUnitEnum.SGD.getDesc())))).multiply(exchangeRate)
						.multiply(tenThousand)
						.setScale(2, RoundingMode.DOWN);
			}
		} else if (registeredCapital.contains(GlobalConstant.UNIT_THOUSAND.concat(GlobalConstant.UNIT))) {
			registeredCapitalNew = new BigDecimal(registeredCapital.substring(0, registeredCapital.indexOf(GlobalConstant.UNIT_THOUSAND.concat(GlobalConstant.UNIT)))).multiply(tenThousand)
					.setScale(2, RoundingMode.DOWN);
		} else {
			registeredCapitalNew = new BigDecimal("0.00");
		}
		return registeredCapitalNew;
	}

}
