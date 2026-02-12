package cn.idicc.taotie.infrastructment.dao.data;

import cn.idicc.taotie.infrastructment.enums.BooleanEnum;
import cn.idicc.taotie.infrastructment.mapper.data.OrgIndustryChainRelationMapper;
import cn.idicc.taotie.infrastructment.entity.data.OrgIndustryChainRelationDO;
import cn.idicc.taotie.infrastructment.request.data.OrgIndustryChainRelationQueryRequest;
import cn.idicc.taotie.infrastructment.response.data.OrgIndustryChainRelationDTO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author wd
 * @description 机构产业链关系
 * @date 12/19/22 4:14 PM
 */
@Component
public class OrgIndustryChainRelationDao extends ServiceImpl<OrgIndustryChainRelationMapper, OrgIndustryChainRelationDO> {

    @Autowired
    OrgIndustryChainRelationMapper orgIndustryChainRelationMapper;

    /**
     * 查询机构产业链关系
     *
     * @param queryRequest
     * @return
     */
    public List<OrgIndustryChainRelationDTO> listByOrgId(OrgIndustryChainRelationQueryRequest queryRequest) {
        return orgIndustryChainRelationMapper.listByOrgId(queryRequest);
    }

    /**
     * 根据机构id查询关联的产业链数据
     *
     * @param organizeId
     * @return
     */
    public List<OrgIndustryChainRelationDO> listByOrgId(Long organizeId) {
        return orgIndustryChainRelationMapper.selectList(Wrappers.lambdaQuery(OrgIndustryChainRelationDO.class)
                .eq(organizeId != null, OrgIndustryChainRelationDO::getOrganizeId, organizeId)
                .eq(OrgIndustryChainRelationDO::getDeleted, BooleanEnum.NO.getCode())
                .eq(OrgIndustryChainRelationDO::getStatus, Boolean.TRUE));
    }

}
