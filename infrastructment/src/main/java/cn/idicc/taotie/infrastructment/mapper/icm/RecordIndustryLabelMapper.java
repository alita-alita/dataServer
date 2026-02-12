/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产业链标签Mapper
 *
 * @author wd
 * @version $Id: IndustryLabelMapper.java,v 0.1 2020.10.10 auto Exp $
 */
@Mapper
public interface RecordIndustryLabelMapper extends BaseMapper<RecordIndustryLabelDO> {
    Long getMaxBizId();

    List<RecordIndustryLabelDO> getLabelByNodeId(@Param("nodeId") Long nodeId);

    List<RecordIndustryLabelDO> getRelationErrorCount(@Param("chainId") Long chainId);


    RecordIndustryLabelDO selectOneByLabelName(@Param("labelName") String labelName);

    int updateById(@Param("labelName") String labelName,
                   @Param("labelDesc") String labelDesc,
                   @Param("deleted") int deleted,
                   @Param("id") Long id);

    long countForJobDetailByChainId(@Param("chainId") Long chainId,
                        @Param("status") Integer status);

    List<RecordIndustryLabelDO> getLabelByChainId(@Param("chainId") Long chainId);
}