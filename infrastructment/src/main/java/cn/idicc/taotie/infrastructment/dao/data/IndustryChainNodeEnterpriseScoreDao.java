package cn.idicc.taotie.infrastructment.dao.data;

import cn.idicc.taotie.infrastructment.mapper.data.IndustryChainNodeEnterpriseScoreMapper;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeEnterpriseScoreDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wd
 * @description 产业链节点企业评分信息dao
 * @date 12/19/22 4:14 PM
 */
@Component
@Slf4j
public class IndustryChainNodeEnterpriseScoreDao extends ServiceImpl<IndustryChainNodeEnterpriseScoreMapper, IndustryChainNodeEnterpriseScoreDO> {

    @Autowired
    IndustryChainNodeEnterpriseScoreMapper industryChainNodeEnterpriseScoreMapper;

    /**
     * 根据条件查询节点企业评分信息
     * @param nodeIds
     * @param unifiedSocialCreditCodes
     * @return
     */
    public List<IndustryChainNodeEnterpriseScoreDO> list(List<Long> nodeIds, List<String> unifiedSocialCreditCodes){
        return industryChainNodeEnterpriseScoreMapper.selectList(Wrappers.lambdaQuery(IndustryChainNodeEnterpriseScoreDO.class)
        .in(IndustryChainNodeEnterpriseScoreDO::getNodeId,nodeIds)
        .in(IndustryChainNodeEnterpriseScoreDO::getUnifiedSocialCreditCode,unifiedSocialCreditCodes));
    }

}
