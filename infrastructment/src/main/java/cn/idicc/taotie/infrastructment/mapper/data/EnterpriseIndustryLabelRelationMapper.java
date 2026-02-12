/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.infrastructment.mapper.data;

import cn.idicc.taotie.infrastructment.entity.data.EnterpriseIndustryLabelRelationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业产业链标签关系Mapper
 *
 * @author wd
 * @version $Id: EnterpriseIndustryLabelRelationMapper.java,v 0.1 2020.10.10 auto Exp $
 */
@Mapper
public interface EnterpriseIndustryLabelRelationMapper extends BaseMapper<EnterpriseIndustryLabelRelationDO> {


    EnterpriseIndustryLabelRelationDO selectByIdWithDeleted(@Param("id") Long id);


    List<Long> selectIdByChainId(@Param("chain_id")Long chainId);
}