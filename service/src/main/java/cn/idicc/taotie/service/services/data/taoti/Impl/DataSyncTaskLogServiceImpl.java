package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.entity.spider.DataSyncItemDO;
import cn.idicc.taotie.infrastructment.entity.spider.DataSyncTaskLogDO;
import cn.idicc.taotie.infrastructment.mapper.spider.DataSyncItemMapper;
import cn.idicc.taotie.infrastructment.mapper.spider.DataSyncTaskLogMapper;
import cn.idicc.taotie.infrastructment.response.data.DataSyncTaskLogResponse;
import cn.idicc.taotie.service.services.data.taoti.DataSyncTaskLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据同步执行日志表 - 服务实现类
 *
 * @author taotie
 * @date 2026-01-27
 */
@Service
public class DataSyncTaskLogServiceImpl implements DataSyncTaskLogService {

    @Autowired
    private DataSyncItemMapper dataSyncItemMapper;
    @Autowired
    private DataSyncTaskLogMapper dataSyncTaskLogMapper;

    @Override
    public DataSyncTaskLogResponse getDataSyncTaskLog(Long itemId, Long chainId) {
        LambdaQueryWrapper<DataSyncTaskLogDO> queryWrapper =
                Wrappers.lambdaQuery(DataSyncTaskLogDO.class)
                        .eq(DataSyncTaskLogDO::getItemId, itemId)
                        .eq(DataSyncTaskLogDO::getChainId, chainId)
                        .last("limit 1");
        DataSyncTaskLogDO dataSyncTaskLogDO = dataSyncTaskLogMapper.selectOne(queryWrapper);
        if (dataSyncTaskLogDO == null) {
            dataSyncTaskLogDO = new DataSyncTaskLogDO();
            dataSyncTaskLogDO.setItemId(itemId);
            dataSyncTaskLogDO.setChainId(chainId);
            dataSyncTaskLogDO.setPlatformCount(0);
            dataSyncTaskLogDO.setMartCount(0);
            dataSyncTaskLogDO.setProdCount(0);
        }
        List<DataSyncItemDO> dataSyncItemDOList = dataSyncItemMapper.selectList(new QueryWrapper<>());
        Map<Long, DataSyncItemDO> dataSyncItemDOMap = dataSyncItemDOList.stream().collect(Collectors.toMap(DataSyncItemDO::getId, item -> item));
        return this.convertToResponse(dataSyncTaskLogDO, dataSyncItemDOMap);
    }

    @Override
    public List<DataSyncTaskLogResponse> queryAll(Long chainId) {
        LambdaQueryWrapper<DataSyncTaskLogDO> queryWrapper =
                Wrappers.lambdaQuery(DataSyncTaskLogDO.class)
                        .eq(DataSyncTaskLogDO::getChainId, chainId);
        List<DataSyncTaskLogDO> dataSyncTaskLogDOS = dataSyncTaskLogMapper.selectList(queryWrapper);
        Map<String, DataSyncTaskLogDO> dataSyncTaskLogDOMap = dataSyncTaskLogDOS.stream().collect(Collectors.toMap(item -> item.getItemId() + ":" + item.getChainId(), item -> item));
        List<DataSyncItemDO> dataSyncItemDOList = dataSyncItemMapper.selectList(new QueryWrapper<>());
        for (DataSyncItemDO dataSyncItemDO : dataSyncItemDOList) {
            DataSyncTaskLogDO dataSyncTaskLogDO = dataSyncTaskLogDOMap.get(dataSyncItemDO.getId() + ":" + chainId);
            if (dataSyncTaskLogDO == null) {
                dataSyncTaskLogDO = new DataSyncTaskLogDO();
                dataSyncTaskLogDO.setChainId(chainId);
                dataSyncTaskLogDO.setItemId(dataSyncItemDO.getId());
                dataSyncTaskLogDO.setPlatformCount(0);
                dataSyncTaskLogDO.setMartCount(0);
                dataSyncTaskLogDO.setProdCount(0);
                dataSyncTaskLogDOS.add(dataSyncTaskLogDO);
            }
        }
        Map<Long, DataSyncItemDO> dataSyncItemDOMap = dataSyncItemDOList.stream().collect(Collectors.toMap(DataSyncItemDO::getId, item -> item));
        return dataSyncTaskLogDOS.stream().map(item -> this.convertToResponse(item, dataSyncItemDOMap)).collect(Collectors.toList());
    }

    /**
     * 将DO对象转换为响应对象
     *
     * @param dataSyncTaskLogDO 数据对象
     * @return 响应对象
     */
    private DataSyncTaskLogResponse convertToResponse(DataSyncTaskLogDO dataSyncTaskLogDO, Map<Long, DataSyncItemDO> dataSyncItemDOMap) {
        if (dataSyncTaskLogDO == null) {
            return null;
        }

        DataSyncTaskLogResponse response = new DataSyncTaskLogResponse();
        response.setId(dataSyncTaskLogDO.getId());
        response.setDiffCalcTime(dataSyncTaskLogDO.getDiffCalcTime());
        response.setDiffCalcStatus(dataSyncTaskLogDO.getDiffCalcStatus());
        response.setPlatformToMartTime(dataSyncTaskLogDO.getPlatformToMartTime());
        response.setPlatformToMartStatus(dataSyncTaskLogDO.getPlatformToMartStatus());
        response.setPlatformToProdTime(dataSyncTaskLogDO.getPlatformToProdTime());
        response.setPlatformToProdStatus(dataSyncTaskLogDO.getPlatformToProdStatus());
        response.setMartToPlatformTime(dataSyncTaskLogDO.getMartToPlatformTime());
        response.setMartToPlatformStatus(dataSyncTaskLogDO.getMartToPlatformStatus());
        response.setPlatformCount(dataSyncTaskLogDO.getPlatformCount());
        response.setMartCount(dataSyncTaskLogDO.getMartCount());
        response.setProdCount(dataSyncTaskLogDO.getProdCount());
        response.setItemId(dataSyncTaskLogDO.getItemId());
        DataSyncItemDO dataSyncItemDO = dataSyncItemDOMap.get(dataSyncTaskLogDO.getItemId());
        if (dataSyncItemDO != null) {
            response.setItemName(dataSyncItemDO.getItemName());
        }
        response.setChainId(dataSyncTaskLogDO.getChainId());
        response.setGmtCreate(dataSyncTaskLogDO.getGmtCreate());
        response.setGmtModify(dataSyncTaskLogDO.getGmtModify());

        return response;
    }
}