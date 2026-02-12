package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentEnterpriseRelationModelMapper;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseRelationModelDO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentEnterpriseRelationModelService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/6/8
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class InvestmentEnterpriseRelationModelServiceImpl extends ServiceImpl<InvestmentEnterpriseRelationModelMapper, InvestmentEnterpriseRelationModelDO> implements InvestmentEnterpriseRelationModelService {

    @Autowired
    private InvestmentEnterpriseRelationModelMapper investmentEnterpriseRelationModelMapper;

    @Override
    public List<Integer> listTypesByInvestmentEnterpriseId(Long investmentEnterpriseId) {
        List<Integer> result = CollectionUtil.newArrayList();
        List<InvestmentEnterpriseRelationModelDO> list = listByInvestmentEnterpriseId(investmentEnterpriseId);
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.stream().map(InvestmentEnterpriseRelationModelDO::getType).distinct().collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<InvestmentEnterpriseRelationModelDO> listByInvestmentEnterpriseId(Long investmentEnterpriseId) {
        return investmentEnterpriseRelationModelMapper.selectList(Wrappers.lambdaQuery(InvestmentEnterpriseRelationModelDO.class)
                .eq(InvestmentEnterpriseRelationModelDO::getDeleted, Boolean.FALSE)
                .eq(InvestmentEnterpriseRelationModelDO::getInvestmentEnterpriseId, investmentEnterpriseId));
    }
}
