package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.EnterpriseCorrelationLabelMapper;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseCorrelationLabelDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseCorrelationLabelService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2022/12/27
 * @Description:
 * @version: 1.0
 */
@Service
public class EnterpriseCorrelationLabelServiceImpl extends ServiceImpl<EnterpriseCorrelationLabelMapper, EnterpriseCorrelationLabelDO> implements EnterpriseCorrelationLabelService {


    @Autowired
    private EnterpriseCorrelationLabelMapper enterpriseCorrelationLabelMapper;

    @Override
    public Map<Long, List<EnterpriseCorrelationLabelDO>> mapByEnterpriseIds(List<Long> enterpriseIds) {
        if (CollectionUtil.isEmpty(enterpriseIds)) {
            throw new BizException("传入企业id集合不能为null");
        }

        Map<Long, List<EnterpriseCorrelationLabelDO>> result = new ConcurrentHashMap<>();
        List<EnterpriseCorrelationLabelDO> list = enterpriseCorrelationLabelMapper.selectList(Wrappers.lambdaQuery(new EnterpriseCorrelationLabelDO())
                .eq(EnterpriseCorrelationLabelDO::getDeleted, Boolean.FALSE)
                .in(EnterpriseCorrelationLabelDO::getEnterpriseId, enterpriseIds));
        if (CollectionUtil.isNotEmpty(list)){
            result = list.stream().collect(Collectors.groupingBy(EnterpriseCorrelationLabelDO::getEnterpriseId));
        }
        return result;
    }
}
