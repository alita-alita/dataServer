package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.entity.spider.DataSyncDiffRecordDO;
import cn.idicc.taotie.infrastructment.entity.spider.DataSyncItemDO;
import cn.idicc.taotie.infrastructment.entity.spider.DataSyncTaskLogDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.spider.DataSyncDiffRecordMapper;
import cn.idicc.taotie.infrastructment.mapper.spider.DataSyncItemMapper;
import cn.idicc.taotie.infrastructment.mapper.spider.DataSyncTaskLogMapper;
import cn.idicc.taotie.infrastructment.request.spider.DataSyncItemRequest;
import cn.idicc.taotie.infrastructment.response.data.DataSyncDiffRecordResponse;
import cn.idicc.taotie.infrastructment.response.data.DataSyncDiffRecordDetailResponse;
import cn.idicc.taotie.infrastructment.response.data.DataSyncItemResponse;
import cn.idicc.taotie.service.result.CommonPageDTO;
import cn.idicc.taotie.service.services.data.taoti.DataSyncItemService;
import cn.idicc.taotie.service.task.SparkTaskExecutor;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据同步任务数据项表 - 服务实现类
 *
 * @author guyongliang
 * @date 2026-01-27
 */
@Service
public class DataSyncItemServiceImpl implements DataSyncItemService {

    @Value("${task.python.env:prod}")
    private String ENV;

    @Autowired
    private DataSyncTaskLogMapper dataSyncTaskLogMapper;
    @Autowired
    private DataSyncItemMapper dataSyncItemMapper;
    @Autowired
    private SparkTaskExecutor sparkTaskExecutor;
    @Autowired
    private DataSyncDiffRecordMapper dataSyncDiffRecordMapper;

    @Override
    public Long save(DataSyncItemRequest request) {
        // 检查itemName是否已存在
        if (isItemNameExists(request.getItemName(), null)) {
            throw new BizException("数据项名称已存在，请使用其他名称");
        }
        
        DataSyncItemDO dataSyncItemDO = new DataSyncItemDO();
        dataSyncItemDO.setItemName(request.getItemName());
        dataSyncItemDO.setDiffCalc(request.getDiffCalc());
        dataSyncItemDO.setPlatformToMart(request.getPlatformToMart());
        dataSyncItemDO.setMartToPlatform(request.getMartToPlatform());
        dataSyncItemDO.setPlatformToProd(request.getPlatformToProd());
        dataSyncItemDO.setTaskName(request.getTaskName());
        dataSyncItemMapper.insert(dataSyncItemDO);
        return dataSyncItemDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        int result = dataSyncItemMapper.deleteById(id);
        LambdaQueryWrapper<DataSyncTaskLogDO> wrapper = new LambdaQueryWrapper<>(DataSyncTaskLogDO.class);
        wrapper.eq(DataSyncTaskLogDO::getItemId, id);
        dataSyncTaskLogMapper.delete(wrapper);
        return result > 0;
    }

    @Override
    public Boolean update(DataSyncItemRequest request) {
        if (request.getId() == null) {
            throw new BizException("数据项ID不能为空");
        }
        
        // 检查itemName是否已存在（排除当前更新的记录）
        if (isItemNameExists(request.getItemName(), request.getId())) {
            throw new BizException("数据项名称已存在，请使用其他名称");
        }

        DataSyncItemDO dataSyncItemDO = new DataSyncItemDO();
        dataSyncItemDO.setId(request.getId());
        dataSyncItemDO.setItemName(request.getItemName());
        dataSyncItemDO.setDiffCalc(request.getDiffCalc());
        dataSyncItemDO.setPlatformToMart(request.getPlatformToMart());
        dataSyncItemDO.setMartToPlatform(request.getMartToPlatform());
        dataSyncItemDO.setPlatformToProd(request.getPlatformToProd());
        dataSyncItemDO.setTaskName(request.getTaskName());

        int result = dataSyncItemMapper.updateById(dataSyncItemDO);
        return result > 0;
    }

    @Override
    public DataSyncItemResponse getById(Long id) {
        if (id == null) {
            return null;
        }

        DataSyncItemDO dataSyncItemDO = dataSyncItemMapper.selectById(id);
        if (dataSyncItemDO == null || dataSyncItemDO.getDeleted()) {
            return null;
        }

        return convertToResponse(dataSyncItemDO);
    }

    @Override
    public List<DataSyncItemResponse> listAll() {
        LambdaQueryWrapper<DataSyncItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSyncItemDO::getDeleted, false); // 过滤已删除的数据

        List<DataSyncItemDO> dataSyncItemDOList = dataSyncItemMapper.selectList(wrapper);
        return dataSyncItemDOList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommonPageDTO<DataSyncDiffRecordDetailResponse> queryDiffDataPage(Long itemId, Long chainId, Integer pageNum, Integer pageSize) {
        Page<DataSyncDiffRecordDO> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<DataSyncDiffRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSyncDiffRecordDO::getItemId, itemId);
        wrapper.eq(DataSyncDiffRecordDO::getChainId, chainId);
        wrapper.eq(DataSyncDiffRecordDO::getDeleted, false);
        
        page = dataSyncDiffRecordMapper.selectPage(page, wrapper);

        List<DataSyncDiffRecordDetailResponse> data = page.getRecords().stream()
                .map(this::convertDiffRecordToResponse)
                .collect(Collectors.toList());
        return CommonPageDTO.of(data, page.getSize(), page.getCurrent(), page.getTotal());
    }

    @Override
    public CommonPageDTO<DataSyncItemResponse> pageList(String itemName, Integer pageNum, Integer pageSize) {
        Page<DataSyncItemDO> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<DataSyncItemDO> queryWrapper =
                Wrappers.lambdaQuery(DataSyncItemDO.class)
                        .like(StringUtils.hasText(itemName), DataSyncItemDO::getItemName, itemName)
                        .orderByDesc(DataSyncItemDO::getId);

        page = dataSyncItemMapper.selectPage(page, queryWrapper);

        List<DataSyncItemResponse> data = page.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return CommonPageDTO.of(data, page.getSize(), page.getCurrent(), page.getTotal());
    }

    @Override
    public Boolean submitDataFlowTask(Long itemId, Long chainId, Integer operateType) {
        DataSyncItemDO dataSyncItemDO = dataSyncItemMapper.selectById(itemId);
        if (dataSyncItemDO == null) {
            throw new BizException("数据项不存在");
        }
        DataSyncTaskLogDO dataSyncTaskLogDO = dataSyncTaskLogMapper.selectOne(Wrappers.<DataSyncTaskLogDO>lambdaQuery()
                .eq(DataSyncTaskLogDO::getItemId, itemId)
                .eq(DataSyncTaskLogDO::getChainId, chainId));
        if (dataSyncTaskLogDO != null) {
            if (dataSyncTaskLogDO.getDiffCalcStatus() == 1) {
                throw new BizException("数据项正在计算差异中，请稍后再试");
            }
            if (dataSyncTaskLogDO.getPlatformToMartStatus() == 1) {
                throw new BizException("数据项正在同步中(平台->集市)，请稍后再试");
            }
            if (dataSyncTaskLogDO.getMartToPlatformStatus() == 1) {
                throw new BizException("数据项正在同步中(集市->平台)，请稍后再试");
            }
            if (dataSyncTaskLogDO.getPlatformToProdStatus() == 1) {
                throw new BizException("数据项正在同步中(平台->生产)，请稍后再试");
            }
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("chainId", chainId);
        params.put("operateType", operateType);
        params.put("env", ENV);
        params.put("itemId", dataSyncItemDO.getId());
        String jobId = sparkTaskExecutor.commonSubmitTask(dataSyncItemDO.getTaskName(), "data_flow_task", params);
        return jobId != null;
    }

    /**
     * 将DO对象转换为响应对象
     *
     * @param dataSyncItemDO 数据对象
     * @return 响应对象
     */
    private DataSyncItemResponse convertToResponse(DataSyncItemDO dataSyncItemDO) {
        if (dataSyncItemDO == null) {
            return null;
        }

        DataSyncItemResponse response = new DataSyncItemResponse();
        response.setId(dataSyncItemDO.getId());
        response.setItemName(dataSyncItemDO.getItemName());
        response.setDiffCalc(dataSyncItemDO.getDiffCalc());
        response.setPlatformToMart(dataSyncItemDO.getPlatformToMart());
        response.setMartToPlatform(dataSyncItemDO.getMartToPlatform());
        response.setPlatformToProd(dataSyncItemDO.getPlatformToProd());
        response.setTaskName(dataSyncItemDO.getTaskName());
        response.setGmtCreate(dataSyncItemDO.getGmtCreate());
        response.setGmtModify(dataSyncItemDO.getGmtModify());

        return response;
    }
    
    /**
     * 将差异记录DO对象转换为响应对象
     *
     * @param dataSyncDiffRecordDO 差异记录数据对象
     * @return 响应对象
     */
    private DataSyncDiffRecordDetailResponse convertDiffRecordToResponse(DataSyncDiffRecordDO dataSyncDiffRecordDO) {
        if (dataSyncDiffRecordDO == null) {
            return null;
        }

        DataSyncDiffRecordDetailResponse response = new DataSyncDiffRecordDetailResponse();
        response.setId(dataSyncDiffRecordDO.getId());
        response.setItemId(dataSyncDiffRecordDO.getItemId());
        response.setChainId(dataSyncDiffRecordDO.getChainId());
        response.setUniqueKey(dataSyncDiffRecordDO.getProductMd5());
        response.setName(dataSyncDiffRecordDO.getProductName());
        response.setPlatformExists(dataSyncDiffRecordDO.getPlatformExists());
        response.setMartExists(dataSyncDiffRecordDO.getMartExists());
        response.setProdExists(dataSyncDiffRecordDO.getProdExists());

        return response;
    }
    
    /**
     * 检查itemName是否存在（排除指定ID的记录）
     *
     * @param itemName 待检查的数据项名称
     * @param excludeId 需要排除的ID（在更新时使用）
     * @return 如果存在返回true，否则返回false
     */
    private boolean isItemNameExists(String itemName, Long excludeId) {
        LambdaQueryWrapper<DataSyncItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSyncItemDO::getItemName, itemName);
        
        if (excludeId != null) {
            queryWrapper.ne(DataSyncItemDO::getId, excludeId); // 排除当前更新的记录
        }
        
        return dataSyncItemMapper.selectCount(queryWrapper) > 0;
    }
}