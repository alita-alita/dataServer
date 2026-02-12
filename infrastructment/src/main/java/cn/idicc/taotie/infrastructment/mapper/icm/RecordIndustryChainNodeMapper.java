/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产业链节点Mapper
 *
 * @author wd
 * @version $Id: IndustryChainNodeMapper.java,v 0.1 2020.10.10 auto Exp $
 */
@Mapper
public interface RecordIndustryChainNodeMapper extends BaseMapper<RecordIndustryChainNodeDO> {

    Long getMaxBizId();

    Integer physicsBatchDeleteByBizIds(@Param("biz_ids") List<Long> bizIds);


    Integer physicsBatchDeleteByChainId(@Param("chain_id") Long chainId);

}