/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeLabelRelationDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 产业链节点与产业链标签关联关系Service
 *
 * @author wd
 * @version $Id: IndustryChainNodeLabelRelationService.java,v 0.1 2020.10.10 auto Exp $
 */
public interface IndustryChainNodeLabelRelationService extends IService<IndustryChainNodeLabelRelationDO> {
    /**
     * 获取指定产业链标签id集合关联的产业链节点集合
     *
     * @param labelIds
     * @return
     */
    List<IndustryChainNodeLabelRelationDO> listByLabelIds(List<Long> labelIds);

    List<String> getLabelByNode(Long nodeId);
}
