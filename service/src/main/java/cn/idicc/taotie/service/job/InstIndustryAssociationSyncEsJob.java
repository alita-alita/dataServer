package cn.idicc.taotie.service.job;


import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.dto.EnterpriseAndChainDTO;
import cn.idicc.taotie.infrastructment.dto.InstindustryAssociationDTO;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.IndustryEnterpriseChainNodeScoreDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.InstIndustryAssociationDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.InstIndustryAssociationMemberDO;
import cn.idicc.taotie.infrastructment.mapper.data.IndustryChainNodeMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.IndustryEnterpriseChainNodeScoreMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.InstIndustryAssociationMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.InstIndustryAssociationMemberMapper;
import cn.idicc.taotie.service.services.data.knowledge.strategy.config.KnowledgeStrategyEnum;
import cn.idicc.taotie.service.services.data.knowledge.strategy.context.KnowledgeSyncContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author guyongliang
 */
@Slf4j
@Component
public class InstIndustryAssociationSyncEsJob {

    @Autowired
    private InstIndustryAssociationMapper instIndustryAssociationMapper;

    @Autowired
    private IndustryChainNodeMapper industryChainNodeMapper;

    @Autowired
    private InstIndustryAssociationMemberMapper instIndustryAssociationMemberMapper;

    @Autowired
    private IndustryEnterpriseChainNodeScoreMapper industryEnterpriseChainNodeScoreMapper;

    @Autowired
    private KnowledgeSyncContext knowledgeSyncContext;

    private final ExecutorService executor = Executors.newFixedThreadPool(8);
    private final Semaphore taskSemaphore = new Semaphore(8);



    @PreDestroy
    public void destroy() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    private class syncEs implements Runnable {
        private final List<InstindustryAssociationDTO> saveData;

        public syncEs(List<InstindustryAssociationDTO> saveData) {
            this.saveData = saveData;
        }

        @Override
        public void run() {
            int retryCount = 0;
            int maxRetries = 3;
            while (retryCount < maxRetries) {
                try {
                    log.info("start sync es, size:{}, retry:{}", saveData.size(), retryCount);
                    long startTime = System.currentTimeMillis();
//                    knowledgeSyncContext.executeSync(KnowledgeStrategyEnum.INST_INDUSTRY_ASSOCIATION_SYNC_STRATEGY, saveData);
                    long endTime = System.currentTimeMillis();
                    log.info("----end sync es, cost time: {}ms", endTime - startTime);
                    return;
                } catch (Exception e) {
                    retryCount++;
                    log.warn("sync es error, retry {}/{}: {}", retryCount, maxRetries, e.getMessage());

                    if (retryCount >= maxRetries) {
                        log.error("sync es failed after {} retries, data size:{}", maxRetries, saveData.size(), e);
                    } else {
                        try {
                            Thread.sleep(1000 * retryCount);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        }
    }

    @XxlJob("IndustryAssociationSyncEsJob")
    public void doSyncEs() {
        int pageSize = 1000;
        int currentPage = 1;
        try {
            List<CompletableFuture<Void>> futureList = new CopyOnWriteArrayList<>();

            // 先查询总数以验证数据存在
            long total = instIndustryAssociationMapper.selectCount(Wrappers.lambdaQuery(InstIndustryAssociationDO.class));
            log.info("行业协会总记录数: {}", total);
            if (total == 0) {
                log.warn("行业协会数据为空，无需同步");
                return;
            }

            Page<InstIndustryAssociationDO> page = new Page<>(currentPage, pageSize);
            log.info("获取第{}页数据，页面大小: {}", currentPage, pageSize);
            Page<InstIndustryAssociationDO> instIndustryAssociationDOPage = instIndustryAssociationMapper.selectPage(page, Wrappers.lambdaQuery(InstIndustryAssociationDO.class));
            log.info("第{}页数据获取完成，记录数: {}", currentPage, instIndustryAssociationDOPage != null ? instIndustryAssociationDOPage.getRecords().size() : "null");

            Map<Long, Long> chainIdAndNodeIdMap = industryChainNodeMapper.selectList(
                    Wrappers.<IndustryChainNodeDO>lambdaQuery()
                            .eq(IndustryChainNodeDO::getNodeLevel, 1)
                            .select(IndustryChainNodeDO::getChainId, IndustryChainNodeDO::getId)
            ).stream().collect(Collectors.toMap(
                    IndustryChainNodeDO::getId,
                    IndustryChainNodeDO::getChainId
            ));
            log.info("加载产业链映射关系完成，映射数量: {}", chainIdAndNodeIdMap.size());

            int pageCount = 0;
            while (true) {
                log.info("处理第{}页数据, instIndustryAssociationDOPage是否为null: {}, 记录是否为空: {}",
                        currentPage,
                        instIndustryAssociationDOPage == null,
                        instIndustryAssociationDOPage != null ? CollectionUtil.isEmpty(instIndustryAssociationDOPage.getRecords()) : "N/A");

                if (instIndustryAssociationDOPage == null || CollectionUtil.isEmpty(instIndustryAssociationDOPage.getRecords())) {
                    log.info("没有更多数据需要处理，退出循环。总页数: {}", pageCount);
                    break;
                }

                pageCount++;
                taskSemaphore.acquire();
                List<InstindustryAssociationDTO> finalInstIndustryAssociationDOList = processIndustryAssciationData(instIndustryAssociationDOPage,chainIdAndNodeIdMap);
                CompletableFuture<Void> future = CompletableFuture.runAsync(new syncEs(finalInstIndustryAssociationDOList), executor).whenComplete(
                        (res, ex) -> {
                            taskSemaphore.release();
                            if (ex != null) {
                                log.error("任务执行失败", ex);
                            }
                        });
                futureList.add(future);
                log.warn("当前页: {}, 页面大小: {}", currentPage, pageSize);

                // 检查是否已经处理完所有数据
                if (instIndustryAssociationDOPage.getRecords().size() < pageSize) {
                    log.info("当前页数据少于页面大小，已处理完所有数据");
                    break;
                }

                currentPage++;
                page = new Page<>(currentPage, pageSize);
                log.info("获取第{}页数据，页面大小: {}", currentPage, pageSize);
                instIndustryAssociationDOPage = instIndustryAssociationMapper.selectPage(page, Wrappers.lambdaQuery(InstIndustryAssociationDO.class));
                log.info("第{}页数据获取完成，记录数: {}", currentPage, instIndustryAssociationDOPage != null ? (instIndustryAssociationDOPage.getRecords() != null ? instIndustryAssociationDOPage.getRecords().size() : "记录为null") : "页面为null");
            }
            log.info("所有页面处理完成。总页数: {}", pageCount);
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
            log.info("所有同步任务完成");
        } catch (InterruptedException e) {
            log.error("同步ES出现中断异常", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("同步过程中出现未预期的错误", e);
        }
    }

    private List<InstindustryAssociationDTO> processIndustryAssciationData(Page<InstIndustryAssociationDO> instIndustryAssociationDOPage,  Map<Long, Long> chainIdAndNodeIdMap) {

        List<InstIndustryAssociationDO> collects = instIndustryAssociationDOPage.getRecords();
        Set<Long> nodeidset = chainIdAndNodeIdMap.keySet();

        // 提取所有行业协会ID用于批量查询
        List<String> associationMd5s = collects.stream()
                .map(InstIndustryAssociationDO::getAssociationMd5)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 批量查询所有相关的企业成员信息
        Map<String, List<InstIndustryAssociationMemberDO>> memberMap;
        if (!associationMd5s.isEmpty()) {
            List<InstIndustryAssociationMemberDO> allMembers = instIndustryAssociationMemberMapper.selectList(
                    Wrappers.lambdaQuery(InstIndustryAssociationMemberDO.class)
                            .in(InstIndustryAssociationMemberDO::getIndustryAssociationId, associationMd5s));

            memberMap = allMembers.stream()
                    .filter(member -> member.getIndustryAssociationId() != null)
                    .collect(Collectors.groupingBy(InstIndustryAssociationMemberDO::getIndustryAssociationId));
        } else {
            memberMap = new HashMap<>();
        }

        // 收集所有企业unicode用于后续查询
        Set<String> allUnicode = memberMap.values().stream()
                .flatMap(List::stream)
                .map(InstIndustryAssociationMemberDO::getMemberUniCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 批量查询企业产业链节点评分数据
        List<IndustryEnterpriseChainNodeScoreDO> allScores = new ArrayList<>();
        if (!allUnicode.isEmpty() && !nodeidset.isEmpty()) {
            allScores = industryEnterpriseChainNodeScoreMapper.selectList(
                    Wrappers.lambdaQuery(IndustryEnterpriseChainNodeScoreDO.class)
                            .in(IndustryEnterpriseChainNodeScoreDO::getUniCode, allUnicode)
                            .in(IndustryEnterpriseChainNodeScoreDO::getChainNodeId, nodeidset));
        }

        // 按企业unicode分组评分数据
        Map<String, List<IndustryEnterpriseChainNodeScoreDO>> scoreByUnicode = allScores.stream()
                .filter(score -> score.getUniCode() != null)
                .collect(Collectors.groupingBy(IndustryEnterpriseChainNodeScoreDO::getUniCode));

        // 处理每个行业协会数据
        return collects.stream().map(instIndustryAssociationDO -> {
            // 获取当前协会的成员列表
            List<InstIndustryAssociationMemberDO> instIndustryAssociationMemberDOS =
                    memberMap.getOrDefault(instIndustryAssociationDO.getAssociationMd5(), Collections.emptyList());

            // 提取成员企业的unicode
            List<String> unicode = instIndustryAssociationMemberDOS.stream()
                    .map(InstIndustryAssociationMemberDO::getMemberUniCode)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // 获取这些企业的评分数据
            List<IndustryEnterpriseChainNodeScoreDO> industryEnterpriseChainNodeScoreDOS = unicode.stream()
                    .flatMap(code -> scoreByUnicode.getOrDefault(code, Collections.emptyList()).stream())
                    .collect(Collectors.toList());

            // 统计每个nodeid对应的unicode数量
            Map<Long, Long> nodeIdCountMap = industryEnterpriseChainNodeScoreDOS.stream()
                    .filter(score -> score.getChainNodeId() != null)
                    .collect(Collectors.groupingBy(
                            IndustryEnterpriseChainNodeScoreDO::getChainNodeId,
                            Collectors.mapping(
                                    IndustryEnterpriseChainNodeScoreDO::getUniCode,
                                    Collectors.counting()
                            )
                    ));

            // 获取unicode数量>=20的nodeid集合
            Set<Long> qualifiedNodeIds = nodeIdCountMap.entrySet().stream()
                    .filter(entry -> entry.getValue() != null && entry.getValue() >= 20)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            // 过滤出符合条件的原始数据
            List<IndustryEnterpriseChainNodeScoreDO> result = industryEnterpriseChainNodeScoreDOS.stream()
                    .filter(item -> item.getChainNodeId() != null && qualifiedNodeIds.contains(item.getChainNodeId()))
                    .collect(Collectors.toList());

            Set<EnterpriseAndChainDTO> enterpriseAndChains = result.stream()
                    .filter(item -> {
                        Long nodeId = item.getChainNodeId();
                        if (nodeId == null) {
                            return false;
                        }
                        Long chainId = chainIdAndNodeIdMap.get(nodeId);
                        return chainId != null;
                    })
                    .collect(Collectors.groupingBy(
                            item -> {
                                Long nodeId = item.getChainNodeId();
                                return chainIdAndNodeIdMap.get(nodeId);
                            },
                            Collectors.mapping(
                                    IndustryEnterpriseChainNodeScoreDO::getUniCode,
                                    Collectors.toSet()
                            )
                    ))
                    .entrySet().stream()
                    .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                    .map(entry -> {
                        List<String> enterpriseList = new ArrayList<>(entry.getValue());
                        return new EnterpriseAndChainDTO(
                                enterpriseList,
                                entry.getKey(),
                                (long) enterpriseList.size()
                        );
                    })
                    .collect(Collectors.toSet());

            InstindustryAssociationDTO instindustryAssociationDTO = new InstindustryAssociationDTO();
            instindustryAssociationDTO.setId(instIndustryAssociationDO.getId());
            instindustryAssociationDTO.setAssociationMd5(instIndustryAssociationDO.getAssociationMd5());
            instindustryAssociationDTO.setAssociationName(instIndustryAssociationDO.getAssociationName());
            instindustryAssociationDTO.setInstindustryAssociationUniCode(instIndustryAssociationDO.getUniCode());
            instindustryAssociationDTO.setRegisterAddress(instIndustryAssociationDO.getRegisterAddress());
            instindustryAssociationDTO.setMobile(instIndustryAssociationDO.getMobile());
            instindustryAssociationDTO.setRegionCode(instIndustryAssociationDO.getRegionCode());
            instindustryAssociationDTO.setEnterpriseAndChains(enterpriseAndChains);
            return instindustryAssociationDTO;
        }).collect(Collectors.toList());
    }
}
