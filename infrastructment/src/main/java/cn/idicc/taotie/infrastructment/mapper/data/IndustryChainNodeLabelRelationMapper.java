/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.infrastructment.mapper.data;

import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeLabelRelationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产业链节点与产业链标签关系Mapper
 *
 * @author wd
 * @version $Id: IndustryChainNodeLabelRelationMapper.java,v 0.1 2020.10.10 auto Exp $
 */
@Mapper
public interface IndustryChainNodeLabelRelationMapper extends BaseMapper<IndustryChainNodeLabelRelationDO> {
    List<String> getLabelNameByNodeId(@Param("nodeId") Long nodeId);
}