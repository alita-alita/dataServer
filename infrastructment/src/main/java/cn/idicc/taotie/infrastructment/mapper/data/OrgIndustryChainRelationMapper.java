/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.infrastructment.mapper.data;

import cn.idicc.taotie.infrastructment.entity.data.OrgIndustryChainRelationDO;
import cn.idicc.taotie.infrastructment.request.data.OrgIndustryChainRelationQueryRequest;
import cn.idicc.taotie.infrastructment.response.data.OrgIndustryChainRelationDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机构产业链关系Mapper
 * @author wd
 * @version $Id: OrgIndustryChainRelationMapper.java,v 0.1 2020.10.10 auto Exp $
 */
@Mapper
public interface OrgIndustryChainRelationMapper extends BaseMapper<OrgIndustryChainRelationDO> {

    /**
     * 根据条件查询机构产业链关系
     * @param queryRequest
     * @return
     */
    List<OrgIndustryChainRelationDTO> listByOrgId(@Param("queryDTO") OrgIndustryChainRelationQueryRequest queryRequest);

}