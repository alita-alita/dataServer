package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentInformationMapper;
import cn.idicc.taotie.infrastructment.entity.data.*;
import cn.idicc.taotie.infrastructment.constant.LockConstant;
import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.enums.KeywordTypeEnum;
import cn.idicc.taotie.infrastructment.enums.ReleaseStatusEnum;
import cn.idicc.taotie.infrastructment.enums.UploadFileStatusEnum;
import cn.idicc.taotie.infrastructment.response.data.InvestmentInformationUploadDTO;
import cn.idicc.taotie.service.services.data.pangu.*;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zengtengpeng.annotation.Lock;
import com.zengtengpeng.enums.LockModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description: 招商资讯实现层
 * @version: 1.0
 */
@Slf4j
@Service
public class InvestmentInformationServiceImpl extends ServiceImpl<InvestmentInformationMapper, InvestmentInformationDO> implements InvestmentInformationService {


    @Autowired
    private UploadFileRecordService uploadFileRecordService;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private IndustryChainService industryChainService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private InvestmentInformationMapper investmentInformationMapper;

    @Autowired
    private KeywordDictionaryService keywordDictionaryService;

    /**
     * 批量导入
     *
     * @param list
     * @return
     */
    @Override
    @Lock(keys = "T(cn.idicc.pangu.constant.NamespaceProperties).getNamespaceRedisPrefix()", keyConstant = LockConstant.UPLOAD_INVESTMENT_INFORMATION, lockModel = LockModel.REENTRANT)
    public Set<InvestmentInformationUploadDTO> uploadBatchSave(List<InvestmentInformationUploadDTO> list, Long uploadFileRecordId) {
        Set<InvestmentInformationUploadDTO> errorList = CollectionUtil.newLinkedHashSet();
        UploadFileRecordDO uploadFileRecordDO = uploadFileRecordService.getById(uploadFileRecordId);
        if (Objects.nonNull(uploadFileRecordDO) && UploadFileStatusEnum.TO_BE_PROCESSED.getCode().equals(uploadFileRecordDO.getStatus())) {
            for (InvestmentInformationUploadDTO index : list) {
                DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
                transactionDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
                TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
                try {
                    String title = index.getTitle();
                    String url = index.getUrl();
                    String source = index.getSource();
                    String releaseDate = index.getReleaseDate();
                    String correlationIndustry = index.getCorrelationIndustry();
                    String correlationEnterprise = index.getCorrelationEnterprise();
                    String newsTheme = index.getNewsTheme();
                    String remark = index.getRemark();
                    boolean condition = StringUtils.isNotBlank(title) && StringUtils.isNotBlank(url) &&
                            StringUtils.isNotBlank(source) && StringUtils.isNotBlank(releaseDate) &&
                            StringUtils.isNotBlank(correlationIndustry) && StringUtils.isNotBlank(newsTheme);
                    if (!condition) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    DateUtil.Str2LocalDateTime(releaseDate, DatePattern.NORM_DATETIME_PATTERN);

                    String[] split = StringUtils.split(correlationIndustry, GlobalConstant.SEMICOLON);
                    List<String> correlationIndustryList = CollectionUtil.newArrayList(split);
                    List<IndustryChainDO> industryChainList = industryChainService.list(Wrappers.lambdaQuery(new IndustryChainDO())
                            .eq(IndustryChainDO::getDeleted, Boolean.FALSE)
                            .in(IndustryChainDO::getChainName, correlationIndustryList));
                    if (industryChainList.size() < split.length) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    if (StringUtils.isNotBlank(correlationEnterprise)) {
                        String[] splitByEnterpriseName = StringUtils.split(correlationEnterprise, GlobalConstant.SEMICOLON);
                        List<EnterpriseDO> enterprises = enterpriseService.queryByNames(CollectionUtil.newArrayList(splitByEnterpriseName));
                        if (enterprises.size() < splitByEnterpriseName.length) {
                            errorList.add(index);
                            platformTransactionManager.rollback(transactionStatus);
                            continue;
                        }
                    }

                    if (StringUtils.isNotBlank(remark) && remark.length() > 200) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    String[] splitByNewsTheme = StringUtils.split(newsTheme, GlobalConstant.SEMICOLON);
                    List<KeywordDictionaryDO> keywordList = keywordDictionaryService.list(Wrappers.lambdaQuery(new KeywordDictionaryDO())
                            .eq(KeywordDictionaryDO::getDeleted, Boolean.FALSE)
                            .eq(KeywordDictionaryDO::getKeywordType, KeywordTypeEnum.NEWS_THEME.getCode())
                            .in(KeywordDictionaryDO::getName, splitByNewsTheme));
                    if (keywordList.size() < splitByNewsTheme.length) {
                        errorList.add(index);
                        platformTransactionManager.rollback(transactionStatus);
                        continue;
                    }

                    InvestmentInformationDO investmentInformationDO = InvestmentInformationUploadDTO.adapt(index);
                    investmentInformationDO.setNewsTheme(StringUtils.join(keywordList.stream().map(KeywordDictionaryDO::getName).collect(Collectors.toList()), GlobalConstant.SEMICOLON));
                    investmentInformationDO.setNewsThemeIds(StringUtils.join(keywordList.stream().map(KeywordDictionaryDO::getId).collect(Collectors.toList()), GlobalConstant.SEMICOLON));
                    String userName = uploadFileRecordDO.getCreateBy();
                    investmentInformationDO.setCreateBy(userName);
                    investmentInformationDO.setUpdateBy(userName);
                    investmentInformationDO.setReleaseStatus(ReleaseStatusEnum.PUBLISHED.getCode());
                    investmentInformationMapper.insert(investmentInformationDO);
                    platformTransactionManager.commit(transactionStatus);

                } catch (Exception e) {
                    errorList.add(index);
                    log.error("InvestmentInformationServiceImpl -> batchSave出现异常：", e);
                    platformTransactionManager.rollback(transactionStatus);
                }
            }
        }
        return errorList;
    }

}
