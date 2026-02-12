/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeDO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 产业链节点service类
 *
 * @author wd
 * @version $Id: IndustryChainNodeService.java,v 0.1 2020.10.10 auto Exp $
 */
public interface IndustryChainNodeService extends IService<IndustryChainNodeDO> {

    /**
     * 同步节点数据到es
     *
     * @param chainNodeChangeDTO
     */
    void synChainNodeDataToEs(JSONObject chainNodeChangeDTO);

    /**
     * 获取指定节点的2级节点
     *
     * @param nodeId
     * @return
     */
    IndustryChainNodeDO getSecondNode(Long nodeId);

    void getParentNode(IndustryChainNodeDO node, List<IndustryChainNodeDO> res);

    /**
     * 根据产业链获取获取产业链节点
     *
     * @param chainId
     * @return
     */
    List<IndustryChainNodeDO> getNodeByChain(Long chainId);
}
