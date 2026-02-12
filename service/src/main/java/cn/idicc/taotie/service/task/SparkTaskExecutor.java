package cn.idicc.taotie.service.task;

import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskInstanceDO;
import cn.idicc.taotie.infrastructment.mapper.icm.SyncTaskInstanceMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.SyncTaskMapper;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @Author: MengDa
 * @Date: 2025/2/11
 * @Description:
 * @version: 1.0
 */
@Component
@Slf4j
public class SparkTaskExecutor extends BasicTaskExecutor {

    @Autowired
    private SyncTaskInstanceMapper syncTaskInstanceMapper;

    @Autowired
    private SyncTaskMapper syncTaskMapper;

    @Resource(name = "executor-sync-task")
    private ExecutorService executor;

    public void syncEnterprise(SyncTaskInstanceDO instanceDO, String env, String date) {
        CompletableFuture.runAsync(() -> {
            log.info("企业信息同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList("enterprise"));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业信息同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncEnterprise(instanceDO.getInstanceDesc(), env, date);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业信息同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList("enterprise"), DateUtil.format(new Date()), "pangu_bus_1");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业信息同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("企业信息同步任务执行 END");
        }, executor);

    }

    public void syncEnterpriseIndustryLabel(SyncTaskInstanceDO instanceDO, String env, List<Long> chains) {
        CompletableFuture.runAsync(() -> {
            log.info("企业挂载同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "enterprise",
                    "industry_label",
                    "industry_chain",
                    "enterprise_industry_label_relation"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业挂载同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncEnterpriseIndustryLabelRelation(instanceDO.getInstanceDesc(), env, chains);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业挂载同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList("enterprise_industry_label_relation"), DateUtil.format(new Date()), "pangu_bus_1");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业挂载同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("企业挂载同步任务执行 END");
        }, executor);

    }

    public void syncRankList1_11_20(SyncTaskInstanceDO instanceDO, String env) {
        CompletableFuture.runAsync(() -> {
            log.info("Ranking批量计算同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "industry_chain_node",
                    "industry_chain_node_label_relation",
                    "industry_label",
                    "industry_chain",
                    "administrative_division",
                    "enterprise_industry_label_relation",
                    "enterprise_label",
                    "enterprise_correlation_label",
                    "enterprise",
                    "ranking_list"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("Ranking批量计算同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncRankingList1_11_20(instanceDO.getInstanceDesc(), env);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("Ranking批量计算同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "ranking_list_1",
                    "ranking_list_2",
                    "ranking_list_3",
                    "ranking_list_4",
                    "ranking_list_5",
                    "ranking_list_6",
                    "ranking_list_7",
                    "ranking_list_8",
                    "ranking_list_9",
                    "ranking_list_10",
                    "ranking_list_11",
                    "ranking_list_20"), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("Ranking批量计算同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("Ranking批量计算同步任务执行 END");
        }, executor);

    }

    public void syncRankListBatch(SyncTaskInstanceDO instanceDO, String env, List<Long> chains, List<Integer> metrics) {
        CompletableFuture.runAsync(() -> {
            log.info("RankingBatch同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "ranking_list",
                    "industry_chain",
                    "industry_chain_node"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("RankingBatch同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncRankingListBatch(instanceDO.getInstanceDesc(), env, chains, metrics);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("RankingBatch同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "ranking_list_12",
                    "ranking_list_13",
                    "ranking_list_14",
                    "ranking_list_15",
                    "ranking_list_16",
                    "ranking_list_17",
                    "ranking_list_18",
                    "ranking_list_19"), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("RankingBatch同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("RankingBatch同步任务执行 END");
        }, executor);

    }

    public void syncPatentInnovation(SyncTaskInstanceDO instanceDO, String env, List<Long> chains) {
        CompletableFuture.runAsync(() -> {
            log.info("专利同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "industry_chain",
                    "industry_chain_node",
                    "industry_innovation",
                    "industry_node_innovation",
                    "local_industry_node_innovation"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("专利同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncPatentInnovation(instanceDO.getInstanceDesc(), env, chains);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("专利同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "industry_innovation",
                    "industry_node_innovation",
                    "local_industry_node_innovation"
            ), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("专利同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("专利同步任务执行 END");
        }, executor);

    }

    public void syncIndustryInvestmentFlow(SyncTaskInstanceDO instanceDO, String env, List<Long> chains) {
        CompletableFuture.runAsync(() -> {
            log.info("投资流向同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "industry_chain",
                    "industry_investment_flow"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("投资流向同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncIndustryInvestmentFlow(instanceDO.getInstanceDesc(), env, chains);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("投资流向同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "industry_investment_flow"
            ), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("投资流向同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("投资流向同步任务执行 END");
        }, executor);

    }

    public void syncListedCompany(SyncTaskInstanceDO instanceDO, String env, List<Long> chains) {
        CompletableFuture.runAsync(() -> {
            log.info("上市公司情况同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "industry_chain",
                    "industry_chain_node",
                    "listed_company"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("上市公司情况同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncListedCompany(instanceDO.getInstanceDesc(), env, chains);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("上市公司情况同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "listed_company"
            ), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("上市公司情况同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("上市公司情况同步任务执行 END");
        }, executor);

    }

    public void syncChainNodeAttribute(SyncTaskInstanceDO instanceDO, String env, List<Long> chains) {
        CompletableFuture.runAsync(() -> {
            log.info("强延补同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "chain_node_attribute",
                    "industry_chain",
                    "industry_chain_node"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("强延补同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncChainNodeAttribute(instanceDO.getInstanceDesc(), env, chains);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("强延补同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "chain_node_attribute"
            ), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("强延补同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("强延补同步任务执行 END");
        }, executor);

    }

    public void syncChainNodeAttributeAnalysis(SyncTaskInstanceDO instanceDO, String env, List<Long> chains) {
        CompletableFuture.runAsync(() -> {
            log.info("五力模型同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "chain_node_attribute_analysis",
                    "industry_chain",
                    "industry_chain_node"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("五力模型同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncChainNodeAttributeAnalysis(instanceDO.getInstanceDesc(), env, chains);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("五力模型同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "chain_node_attribute_analysis"
            ), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("五力模型同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("五力模型同步任务执行 END");
        }, executor);

    }

    public void syncEnterpriseLabelTech(SyncTaskInstanceDO instanceDO, String env) {
        CompletableFuture.runAsync(() -> {
            log.info("企业标签(科技)同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "enterprise",
                    "enterprise_label",
                    "enterprise_correlation_label"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(科技)同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncEnterpriseLabelTech(instanceDO.getInstanceDesc(), env);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(科技)同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "enterprise_correlation_label_tech"), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(科技)同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("企业标签(科技)同步任务执行 END");
        }, executor);

    }

    public void syncEnterpriseLabelListed(SyncTaskInstanceDO instanceDO, String env) {
        CompletableFuture.runAsync(() -> {
            log.info("企业标签(上市)同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "enterprise",
                    "enterprise_label",
                    "enterprise_correlation_label"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(上市)同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncEnterpriseLabelListed(instanceDO.getInstanceDesc(), env);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(上市)同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "enterprise_correlation_label_listed"), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(上市)同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("企业标签(上市)同步任务执行 END");
        }, executor);

    }

    public void syncEnterpriseLabelFinancing(SyncTaskInstanceDO instanceDO, String env) {
        CompletableFuture.runAsync(() -> {
            log.info("企业标签(融资)同步任务执行 START");
            //同步ODS
            instanceDO.setStatus("ODS同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String odsJobId = syncOds(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "enterprise",
                    "enterprise_label",
                    "enterprise_correlation_label"
            ));
            Integer odsJobStatus = saveAndWaitForEnd(instanceDO, odsJobId);
            if (!odsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(融资)同步任务执行 FAIL");
                instanceDO.setStatus("ODS同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncEnterpriseLabelFinancing(instanceDO.getInstanceDesc(), env);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(融资)同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            }
            //发送消息
            instanceDO.setStatus("kafka消息同步中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String messageJobId = syncMessage(instanceDO.getInstanceDesc(), env, Arrays.asList(
                    "enterprise_correlation_label_financing"), DateUtil.format(new Date()), "pangu_bus_2");
            Integer messageJobStatus = saveAndWaitForEnd(instanceDO, messageJobId);
            if (!messageJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("企业标签(融资)同步任务执行 FAIL");
                instanceDO.setStatus("kafka消息同步失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("kafka消息同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("企业标签(融资)同步任务执行 END");
        }, executor);

    }

    public void syncEnterpriseXiaoAi(SyncTaskInstanceDO instanceDO, String env, String date) {
        CompletableFuture.runAsync(() -> {
            log.info("小艾企业信息同步任务执行 START");
            //同步ADS
            instanceDO.setStatus("对比计算中");
            syncTaskInstanceMapper.updateById(instanceDO);
            String adsJobId = syncEnterpriseXiaoAi(instanceDO.getInstanceDesc(), env, date);
            Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
            if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                log.error("小艾企业信息同步任务执行 FAIL");
                instanceDO.setStatus("对比计算失败");
                syncTaskInstanceMapper.updateById(instanceDO);
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                return;
            } else {
                instanceDO.setStatus("同步成功");
                syncTaskInstanceMapper.updateById(instanceDO);
            }
            syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
            log.info("小艾企业信息同步任务执行 END");
        }, executor);
    }

    public void syncCommonTask(SyncTaskInstanceDO instanceDO, String env, Map<String, List<String>> paramMap) {
        CompletableFuture.runAsync(() -> {
            try {
                if (paramMap.get("task_name") == null || paramMap.get("task_name").isEmpty()) {
                    log.error("任务名称不能为空");
                    return;
                }
                String taskName = paramMap.get("task_name").get(0);
                Map<String, Object> param = new HashMap<String, Object>() {{
                    put("env", env);
                }};
                log.info("通用同步任务执行 START");
                instanceDO.setStatus("数据同步开始，spark服务调用开始");
                syncTaskInstanceMapper.updateById(instanceDO);
                String adsJobId = commonSubmitTask(taskName, instanceDO.getInstanceDesc(), param);
                if (adsJobId == null) {
                    instanceDO.setStatus("spark服务调用失败");
                    syncTaskInstanceMapper.updateById(instanceDO);
                    return;
                }
                Integer adsJobStatus = saveAndWaitForEnd(instanceDO, adsJobId);
                if (!adsJobStatus.equals(STATUS_EXECUTE_SUCCESS)) {
                    log.error("通用同步任务执行 FAIL");
                    instanceDO.setStatus("spark任务执行失败");
                    syncTaskInstanceMapper.updateById(instanceDO);
                } else {
                    instanceDO.setStatus("spark任务执行成功");
                    syncTaskInstanceMapper.updateById(instanceDO);
                }
            } catch (Exception e) {
                log.error("通用任务错误", e);
                instanceDO.setStatus("通用任务执行失败");
                syncTaskInstanceMapper.updateById(instanceDO);
            } finally {
                syncTaskMapper.updateTaskLock(instanceDO.getTaskId(), false);
                log.info("通用同步任务执行 END");
            }
        }, executor);
    }
}
