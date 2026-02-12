package cn.idicc.taotie.infrastructment.dao.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.enums.BooleanEnum;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryChainNodeMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wd
 * @description 产业链节点dao
 * @date 12/19/22 4:14 PM
 */
@Component
public class RecordIndustryChainNodeDao extends ServiceImpl<RecordIndustryChainNodeMapper, RecordIndustryChainNodeDO> {

    @Resource
    RecordIndustryChainNodeMapper recordIndustryChainNodeMapper;

    /**
     * 根据产业链id、产业链节点名称查询产业链信息
     * @param chanId 产业链id
     * @param nodeName 产业链节点名称
     * @return 返回产业链节点数据
     */
    public List<RecordIndustryChainNodeDO> selectByChanIdAndChanNodeName(Long chanId, String nodeName){
        return recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
                .eq(RecordIndustryChainNodeDO::getDeleted, BooleanEnum.NO.getCode())
                .eq(RecordIndustryChainNodeDO::getChainId,chanId)
                .eq(RecordIndustryChainNodeDO::getNodeName,nodeName));
    }

    /**
     * 根据产业链id查询产业链节点数据
     * @param chainId
     * @return
     */
    public List<RecordIndustryChainNodeDO> list(Long chainId){
        return recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
                .eq(RecordIndustryChainNodeDO::getChainId,chainId)
                .eq(RecordIndustryChainNodeDO::getDeleted,false)
                .orderByAsc(RecordIndustryChainNodeDO::getNodeOrder)
                .orderByAsc(RecordIndustryChainNodeDO::getNodeName));
    }

    /**
     * 根据产业链id查询产业链节点数据
     * @param chainId
     * @return
     */
    public List<RecordIndustryChainNodeDO> listByChainId(Long chainId){
        return recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
                .eq(RecordIndustryChainNodeDO::getChainId,chainId)
                .eq(RecordIndustryChainNodeDO::getDeleted,false)
                .orderByAsc(RecordIndustryChainNodeDO::getNodeOrder)
                .orderByAsc(RecordIndustryChainNodeDO::getNodeName));
    }

    public List<RecordIndustryChainNodeDO> listLeafByChainIdAndVersion(Long chainId){
        return recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
                .eq(RecordIndustryChainNodeDO::getChainId,chainId)
                .eq(RecordIndustryChainNodeDO::getIsLeaf,1)
                .eq(RecordIndustryChainNodeDO::getDeleted,false)
                .orderByAsc(RecordIndustryChainNodeDO::getNodeOrder)
                .orderByAsc(RecordIndustryChainNodeDO::getNodeName));
    }

    public RecordIndustryChainNodeDO getByNodeId(Long nodeId){
        return recordIndustryChainNodeMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
                .eq(RecordIndustryChainNodeDO::getBizId,nodeId));
    }

    /**
     * 根据父节点查询所有的子节点数据
     * @param parentId
     * @return
     */
    public List<RecordIndustryChainNodeDO> queryByNodeParent(Long parentId){
        return recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
                .eq(RecordIndustryChainNodeDO::getDeleted,BooleanEnum.NO.getCode())
                .eq(RecordIndustryChainNodeDO::getNodeParent,parentId));
    }

    /**
     * 根据产业链id父节点id查询节点数据
     * @param chainId
     * @param parentId
     * @return
     */
    public List<RecordIndustryChainNodeDO> queryByNodeParent(Long chainId,Long parentId){
        return recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
                .eq(RecordIndustryChainNodeDO::getChainId,chainId)
                .eq(RecordIndustryChainNodeDO::getNodeParent,parentId));
    }


    /**
     * 根据产业链ids批量查询挂载企业节点数据
     * @param chainIds
     * @return
     */
    public List<RecordIndustryChainNodeDO> queryLeafNodesByChainIds(List<Long> chainIds){
        return recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
                .in(RecordIndustryChainNodeDO::getChainId,chainIds)
                .eq(RecordIndustryChainNodeDO::getIsLeaf,Boolean.TRUE));
    }

}
