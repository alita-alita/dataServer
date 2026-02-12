package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskDO;
import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskInstanceDO;
import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskInstanceLogDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.SyncTaskInstanceLogMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.SyncTaskInstanceMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.SyncTaskMapper;
import cn.idicc.taotie.infrastructment.request.icm.task.JobPageRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.SubmitTaskRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.TaskInstancePageRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.TaskPageRequest;
import cn.idicc.taotie.infrastructment.response.icm.task.TaskInstanceDTO;
import cn.idicc.taotie.infrastructment.response.icm.task.TaskInstanceJobDTO;
import cn.idicc.taotie.service.services.icm.TaskService;
import cn.idicc.taotie.service.task.SparkTaskExecutor;
import cn.idicc.taotie.service.task.logs.TaskLogDTO;
import cn.idicc.taotie.service.util.CommandLineParserUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: MengDa
 * @Date: 2025/2/11
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private SyncTaskMapper syncTaskMapper;

    @Autowired
    private SyncTaskInstanceMapper syncTaskInstanceMapper;

    @Autowired
    private SyncTaskInstanceLogMapper syncTaskInstanceLogMapper;

    @Autowired
    private SparkTaskExecutor sparkTaskExecutor;

    @Value("${task.python.env:prod}")
    private String ENV;

    private static final String TYPE_SPARK = "SPARK";
    private static final String TYPE_DATAX = "DATAX";

    @Override
    public IPage<SyncTaskDO> taskPage(TaskPageRequest request) {
        return syncTaskMapper.selectPage(Page.of(request.getPageNum(), request.getPageSize()), null);
    }

    @Override
    public IPage<TaskInstanceDTO> taskInstancePage(TaskInstancePageRequest request) {
        return syncTaskInstanceMapper.page(Page.of(request.getPageNum(), request.getPageSize()), request.getTaskId());
    }

    @Override
    public IPage<TaskInstanceJobDTO> taskInstanceJobPage(JobPageRequest request) {
        return syncTaskInstanceLogMapper.page(Page.of(request.getPageNum(), request.getPageSize()),
                request.getInstanceId(),
                request.getTaskId());
    }

    @Override
    public List<TaskLogDTO> queryLogByJobId(String jobId, Integer offset, String type) {
        if (offset == null) {
            offset = 0;
        }
        SyncTaskInstanceLogDO logDO = syncTaskInstanceLogMapper.selectOne(Wrappers.lambdaQuery(SyncTaskInstanceLogDO.class)
                .eq(SyncTaskInstanceLogDO::getJobId, jobId));
        if (logDO == null) {
            throw new BizException("不存在的JobId");
        }
        if (logDO.getJobLog() != null && !logDO.getJobLog().isEmpty()) {
            if (type != null) {
                return JSON.parseArray(logDO.getJobLog(), TaskLogDTO.class).stream().filter(e -> e.getType().equals(type)).collect(Collectors.toList());
            }
            return JSON.parseArray(logDO.getJobLog(), TaskLogDTO.class);
        }
        SyncTaskDO taskDO = syncTaskMapper.selectById(logDO.getTaskId());
        if (taskDO == null) {
            throw new BizException("不存在的任务");
        }
        if (taskDO.getTaskType().equals(TYPE_SPARK)) {
            return sparkTaskExecutor.getLogs(jobId, offset, type);
        } else if (taskDO.getTaskType().equals(TYPE_DATAX)) {
            return new ArrayList<>();
        }
        return Collections.emptyList();
    }

    @Override
    public void runTask(SubmitTaskRequest request) {
        SyncTaskDO taskDO = syncTaskMapper.selectById(request.getTaskId());
        if (taskDO == null) {
            throw new BizException("不存在的任务");
        }
        if (taskDO.getIsLock()) {
            throw new BizException("该任务已上锁，请等待执行完毕");
        }
        syncTaskMapper.updateTaskLock(taskDO.getId(), true);
        SyncTaskInstanceDO instanceDO = new SyncTaskInstanceDO();
        instanceDO.setTaskId(taskDO.getId());
        instanceDO.setInstanceDesc(request.getRemark());
        instanceDO.setParam(request.getParam());
        instanceDO.setStatus("待执行");
        syncTaskInstanceMapper.insert(instanceDO);
        Map<String, List<String>> paramMap = CommandLineParserUtil.parseArgs(request.getParam());
        switch (taskDO.getName()) {
            case "enterprise":
                sparkTaskExecutor.syncEnterprise(instanceDO, ENV, paramMap.get("date").get(0));
                break;
            case "enterprise_industry_label_relation":
                sparkTaskExecutor.syncEnterpriseIndustryLabel(instanceDO, ENV,
                        paramMap.get("chains").stream().map(Long::valueOf).collect(Collectors.toList()));
                break;
            case "ranking_list_1_11_20":
                sparkTaskExecutor.syncRankList1_11_20(instanceDO, ENV);
                break;
            case "ranking_list_12_19":
                sparkTaskExecutor.syncRankListBatch(instanceDO, ENV,
                        paramMap.get("chains").stream().map(Long::valueOf).collect(Collectors.toList()),
                        paramMap.get("metrics").stream().map(Integer::valueOf).collect(Collectors.toList())
                );
                break;
            case "patent_innovation":
                sparkTaskExecutor.syncPatentInnovation(instanceDO, ENV,
                        paramMap.get("chains").stream().map(Long::valueOf).collect(Collectors.toList()));
                break;
            case "listed_company":
                sparkTaskExecutor.syncListedCompany(instanceDO, ENV,
                        paramMap.get("chains").stream().map(Long::valueOf).collect(Collectors.toList()));
                break;
            case "industry_investment_flow":
                sparkTaskExecutor.syncIndustryInvestmentFlow(instanceDO, ENV,
                        paramMap.get("chains").stream().map(Long::valueOf).collect(Collectors.toList()));
                break;
            case "chain_node_attribute":
                sparkTaskExecutor.syncChainNodeAttribute(instanceDO, ENV,
                        paramMap.get("chains").stream().map(Long::valueOf).collect(Collectors.toList()));
                break;
            case "chain_node_attribute_analysis":
                sparkTaskExecutor.syncChainNodeAttributeAnalysis(instanceDO, ENV,
                        paramMap.get("chains").stream().map(Long::valueOf).collect(Collectors.toList()));
                break;
            case "enterprise_correlation_label_tech":
                sparkTaskExecutor.syncEnterpriseLabelTech(instanceDO, ENV);
                break;
            case "enterprise_correlation_label_listed":
                sparkTaskExecutor.syncEnterpriseLabelListed(instanceDO, ENV);
                break;
            case "enterprise_correlation_label_financing":
                sparkTaskExecutor.syncEnterpriseLabelFinancing(instanceDO, ENV);
                break;
            case "enterprise_xiao_ai":
                sparkTaskExecutor.syncEnterpriseXiaoAi(instanceDO, ENV, paramMap.get("date").get(0));
                break;
            case "common_task":
                sparkTaskExecutor.syncCommonTask(instanceDO, ENV, paramMap);
                break;
            default:
                throw new BizException("未定义的任务");
        }
    }
}
