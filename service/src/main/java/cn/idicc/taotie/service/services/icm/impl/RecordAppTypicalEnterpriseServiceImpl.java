package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.dto.DwdEnterpriseDTO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAppEnterpriseIndustryChainSuspectedDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAppTypicalEnterpriseDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import cn.idicc.taotie.infrastructment.es.ElasticsearchUtils;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.dw.DwdAllMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.*;
import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterpriseAddRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterprisePageRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterpriseUpdateRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordAppTypicalEnterpriseDTO;
import cn.idicc.taotie.infrastructment.utils.MD5Util;
import cn.idicc.taotie.infrastructment.es.ElasticsearchUtils;
import cn.idicc.taotie.service.services.icm.RecordAppTypicalEnterpriseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: MengDa
 * @Date: 2025/2/10
 * @Description:
 * @version: 1.0
 */
@Service
@Slf4j
public class RecordAppTypicalEnterpriseServiceImpl implements RecordAppTypicalEnterpriseService {

    @Autowired
    private RecordAppTypicalEnterpriseMapper recordAppTypicalEnterpriseMapper;

    @Autowired
    private RecordIndustryLabelMapper recordIndustryLabelMapper;

    @Autowired
    private RecordIndustryChainNodeMapper recordIndustryChainNodeMapper;

    @Autowired
    private RecordAppEnterpriseIndustryChainSuspectedMapper appEnterpriseIndustryChainSuspectedMapper;

    @Autowired
    private RecordIndustryChainMapper recordIndustryChainMapper;

    @Autowired
    private ElasticsearchUtils elasticsearchUtils;

    @Resource
    private DwdAllMapper dwdAllMapper;

    @Autowired
    private IndustryChainAtomNodeMapper industryChainAtomNodeMapper;

    @Override
    public String addTypicalEnterprise(RecordTypicalEnterpriseAddRequest request) {
        // 产业链标签ID
        Long labelId = request.getLabelId();
        // 根据标签ID获取产业标签
        RecordIndustryLabelDO labelDO = recordIndustryLabelMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
                .eq(RecordIndustryLabelDO::getBizId, labelId)
        );
        if (labelDO == null) {
            throw new BizException("标签信息不存在");
        }
        // 解析请求中的企业名称列表
        String enterpriseNames = request.getEnterpriseName();
        List<String> enterpriseNameList = Arrays.stream(enterpriseNames.split(","))
                .map(String::trim) // 去除首尾空格
                .filter(StringUtils::isNotBlank) // 过滤空字符串
                .collect(Collectors.toList());
        if (enterpriseNameList.isEmpty()) {
            throw new BizException("输入错误，企业名称列表为空");
        }
        List<DwdEnterpriseDTO> enterpriseDTOS = dwdAllMapper.queryByNames(enterpriseNameList);
        if (enterpriseDTOS.isEmpty()) {
            throw new BizException("输入错误，没有匹配的企业");
        }
        // 遍历每个企业名称，结合产业链结构信息创建典型企业记录
        int cnt = 0;
        for (DwdEnterpriseDTO enterpriseDTO : enterpriseDTOS) {
            // 统一社会信用代码
            String uniCode = enterpriseDTO.getUniCode();
            // 企业名称
            String enterpriseName = enterpriseDTO.getEnterpriseName();
            log.info("enterpriseDTO: {}:{}", enterpriseName, uniCode);

            if (StringUtils.isBlank(enterpriseName) || StringUtils.isBlank(uniCode)) {
                log.error("数据错误，企业名称或统一社会信用代码为空");
                continue;
            }
            //判断是否已存在
            if (!recordAppTypicalEnterpriseMapper.checkExists(labelDO.getBizId(), uniCode)) {
                RecordAppTypicalEnterpriseDO typicalEnterpriseDO = new RecordAppTypicalEnterpriseDO();
                typicalEnterpriseDO.setIndustryLabelId(labelDO.getBizId());
                typicalEnterpriseDO.setIndustryLabelName(labelDO.getLabelName());
                typicalEnterpriseDO.setEnterpriseId(MD5Util.getMd5Id(uniCode));
                typicalEnterpriseDO.setEnterpriseName(enterpriseName);
                typicalEnterpriseDO.setEnterpriseUniCode(uniCode);
                // 插入数据库
                recordAppTypicalEnterpriseMapper.insert(typicalEnterpriseDO);
                cnt += 1;
            }
        }
        if (cnt == 0) {
            return "无新增数据";
        } else {
            return "新增" + cnt + "条数据";
        }
    }

    @Override
    public Boolean updateTypicalEnterprise(RecordTypicalEnterpriseUpdateRequest request) {
        if ((request.getEnterpriseName() == null || request.getEnterpriseName().isEmpty())
                && (request.getEnterpriseUniCode() == null || request.getEnterpriseUniCode().isEmpty())){
            throw new BizException("无效的修改");
        }
        RecordAppTypicalEnterpriseDO tmp = recordAppTypicalEnterpriseMapper.selectById(request.getId());
        if (tmp.getEnterpriseName() != null && !tmp.getEnterpriseName().isEmpty()
                && tmp.getEnterpriseUniCode() != null && !tmp.getEnterpriseUniCode().isEmpty()
        ){
            throw new BizException("该数据不可修改");
        }
        if (request.getEnterpriseName() != null && !request.getEnterpriseName().isEmpty()){
            tmp.setEnterpriseName(request.getEnterpriseName());
        }
        if (request.getEnterpriseUniCode() != null && !request.getEnterpriseUniCode().isEmpty()){
            tmp.setEnterpriseUniCode(request.getEnterpriseUniCode());
        }
        if ((tmp.getEnterpriseName() == null || tmp.getEnterpriseName().isEmpty())
                || (tmp.getEnterpriseUniCode() == null || tmp.getEnterpriseUniCode().isEmpty())){
            throw new BizException("企业名称及社会信用代码不可为空");
        }
        if (recordAppTypicalEnterpriseMapper.checkExists(tmp.getIndustryLabelId(),tmp.getEnterpriseUniCode())) {
            throw new BizException("已存在的数据");
        }
        tmp.setEnterpriseId(MD5Util.getMd5Id(tmp.getEnterpriseUniCode()));
        recordAppTypicalEnterpriseMapper.updateById(tmp);
        return true;
    }

    @Override
    public Boolean deleteById(Long id) {
        return recordAppTypicalEnterpriseMapper.physicDelete(id) > 0;
    }

    @Override
    public Boolean addAllToSuspected(String chainId) {
        // 产业链ID
        Long industryChainId = Long.parseLong(chainId);
        RecordIndustryChainDO chainDO = recordIndustryChainMapper.selectById(industryChainId);
        if (chainDO == null) {
            throw new BizException("产业链信息不存在");
        }
        // 查询典型企业
        List<RecordAppTypicalEnterpriseDO> enterpriseDOS = recordAppTypicalEnterpriseMapper.queryByChainId(industryChainId);
        enterpriseDOS.forEach(enterpriseDO -> {
            RecordAppEnterpriseIndustryChainSuspectedDO chainSuspectedDO = new RecordAppEnterpriseIndustryChainSuspectedDO();
            chainSuspectedDO.setBizId(MD5Util.getMd5Id(enterpriseDO.getEnterpriseId() + chainId));
            chainSuspectedDO.setEnterpriseId(enterpriseDO.getEnterpriseId());
            chainSuspectedDO.setEnterpriseUniCode(enterpriseDO.getEnterpriseUniCode());
            chainSuspectedDO.setEnterpriseName(enterpriseDO.getEnterpriseName());
            chainSuspectedDO.setIndustryChainId(chainDO.getBizId());
            chainSuspectedDO.setIndustryChainName(chainDO.getChainName());
            chainSuspectedDO.setDataSource("典型企业");
            chainSuspectedDO.setNegative(appEnterpriseIndustryChainSuspectedMapper.isNegative(enterpriseDO.getEnterpriseName()));
            RecordAppEnterpriseIndustryChainSuspectedDO suspectedDO = appEnterpriseIndustryChainSuspectedMapper.selectByBizId(chainSuspectedDO.getBizId());
            if (suspectedDO == null) {
                appEnterpriseIndustryChainSuspectedMapper.insert(chainSuspectedDO);
            } else {
                chainSuspectedDO.setId(suspectedDO.getId());
                appEnterpriseIndustryChainSuspectedMapper.updateById(chainSuspectedDO);
            }
        });
        return true;
    }

    @Override
    public IPage<RecordAppTypicalEnterpriseDTO> pageSearch(RecordTypicalEnterprisePageRequest request) {
        IPage<RecordAppTypicalEnterpriseDO> tmpPage = recordAppTypicalEnterpriseMapper.selectPage(
                new Page<>(request.getPageNum(), request.getPageSize()),
                Wrappers.lambdaQuery(RecordAppTypicalEnterpriseDO.class)
                        .eq(RecordAppTypicalEnterpriseDO::getIndustryLabelId,request.getLabelId())
                );
        IPage<RecordAppTypicalEnterpriseDTO> res = new Page<>();
        BeanUtils.copyProperties(tmpPage,res);
        res.setRecords(tmpPage.getRecords().stream().map(RecordAppTypicalEnterpriseDTO::adapter).collect(Collectors.toList()));
        return res;
    }
}
