/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.request.icm.SaveRecordIndustryChainNodeRequest;
import cn.idicc.taotie.infrastructment.request.icm.UpdateNodeScoreRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainNodeDTO;
import cn.idicc.taotie.infrastructment.response.icm.OrgRecordIndustryChainNodeDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 产业链节点service类
 *
 * @author wd
 * @version $Id: IndustryChainNodeService.java,v 0.1 2020.10.10 auto Exp $
 */
public interface RecordIndustryChainNodeService extends IService<RecordIndustryChainNodeDO> {

    /**
     * 保存产业链节点
     *
     * @param request
     */
    void saveOrUpdate(SaveRecordIndustryChainNodeRequest request);

    void updateThresholdScore(UpdateNodeScoreRequest request);
    /**
     * 根据产业链id查询产业链节点树
     *
     * @param chainId
     * @return
     */
    OrgRecordIndustryChainNodeDTO queryChainNodeTreeByChainId(Long chainId);
    /**
     * 根据产业链id查询产业链节点详情
     *
     * @param id
     * @return
     */
    RecordIndustryChainNodeDTO detail(Long id);

    /**
     * 根据节点id删除节点及其子节点数据
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 查询产业链节点信息
     * @param chainId
     * @return
     */
    List<RecordIndustryChainNodeDO> list(Long chainId);

    /**
     * 查询所有子节点数据
     *
     * @param nodeParentId
     * @param chainNodeDOS
     */
    void queryChildNodes(Long nodeParentId, List<RecordIndustryChainNodeDO> chainNodeDOS);

    List<OrgRecordIndustryChainNodeDTO> queryChainNodeLeafListByChainId(Long chainId);
}
