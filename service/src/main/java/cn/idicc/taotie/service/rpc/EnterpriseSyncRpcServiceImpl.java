package cn.idicc.taotie.service.rpc;

import cn.idicc.taotie.infrastructment.entity.xiaoai.EnterpriseDevelopmentIndexDO;
import cn.idicc.taotie.infrastructment.mapper.data.EnterpriseIndustryLabelRelationMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.EnterpriseAcademiaMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.EnterpriseAncestorMapper;
import cn.idicc.taotie.infrastructment.mapper.xiaoai.EnterpriseDevelopmentIndexMapper;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import cn.idicc.taotie.infrastructment.response.xiaoai.EnterpriseCodesDTO;
import cn.idicc.taotie.provider.api.common.RPCResult;
import cn.idicc.taotie.provider.api.common.RPCResultBuilder;
import cn.idicc.taotie.provider.api.common.RPCStatus;
import cn.idicc.taotie.provider.api.dto.EnterprisePartnerRelationshipDTO;
import cn.idicc.taotie.provider.api.service.EnterpriseSyncRpcService;
import cn.idicc.taotie.service.services.data.knowledge.strategy.config.KnowledgeStrategyEnum;
import cn.idicc.taotie.service.services.data.knowledge.strategy.context.KnowledgeSyncContext;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseProductService;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * @Author: MengDa
 * @Date: 2025/3/10
 * @Description:
 * @version: 1.0
 */
@Slf4j
@DubboService(timeout = 3000)
public class EnterpriseSyncRpcServiceImpl implements EnterpriseSyncRpcService {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private EnterpriseIndustryLabelRelationMapper enterpriseIndustryLabelRelationMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EnterpriseDevelopmentIndexMapper enterpriseDevelopmentIndexMapper;

    @Autowired
    private EnterpriseAcademiaMapper enterpriseAcademiaMapper;

    @Autowired
    private EnterpriseAncestorMapper enterpriseAncestorMapper;

    @Autowired
    private EnterpriseProductService enterpriseProductService;

    @Autowired
    private KnowledgeSyncContext knowledgeSyncContext;

    @Resource(name = "executor-sync-es")
    private ExecutorService executor;

    private static final Long BATCH_SIZE = 5000L;

    @Override
    public RPCResult<String> updateByUnicode(String uniCode) {
        log.info("开始同步企业ES，企业UniCode为：{}", uniCode);
        try{
            enterpriseService.doSyncDataToEs(uniCode);
            enterpriseProductService.doSyncDataToEs(null, uniCode);
        }catch (Exception e){
            log.error("EnterpriseSyncRpcService updateByUnicode fail,e: ",e);
            return RPCResultBuilder.build(RPCStatus.SYSTEM_UNEXCEPT_ERROR, e.getMessage());
        }
        return RPCResultBuilder.build(RPCStatus.SUCCESS, "success");
    }

    @Override
    public RPCResult<String> updateByChain(Long chainId) {
        enterpriseProductService.doSyncDataToEs(chainId, null);
        log.info("开始同步企业产业链信息，产业链ID:{}",chainId);
        List<Long> enterpriseIds = enterpriseIndustryLabelRelationMapper.selectIdByChainId(chainId);
        Set<Long> idSet = new HashSet<>(enterpriseIds);
        log.info("同步企业数量：{}",idSet.size());
        for(Long id:idSet) {
            executor.submit(() -> {
                try{
                    log.info("同步企业产业链信息中，产业链ID:{}，企业ID:{}",chainId,id);
                    enterpriseService.doSyncDataToEs(id);
                }catch (Exception e){
                    log.error("同步企业异常：",e);
                }
            });
        }
        log.info("企业产业链信息同步结束");
        return RPCResultBuilder.build(RPCStatus.SUCCESS, "success");
    }

    @Override
    public RPCResult<String> updateAllDevelopmentIndex() {
        log.info("开始同步企业指标信息");
        IndexCoordinates indexCoordinates = elasticsearchRestTemplate.getIndexCoordinatesFor(EnterprisePO.class);
        IPage<EnterpriseDevelopmentIndexDO> page = enterpriseDevelopmentIndexMapper.selectPage(Page.of(1,BATCH_SIZE),null);
        AtomicInteger cnt = new AtomicInteger(0);
        for (int i=1;i<=page.getPages();i++){
            page=enterpriseDevelopmentIndexMapper.selectPage(Page.of(i,BATCH_SIZE),null);
            long total = page.getTotal();
            for(EnterpriseDevelopmentIndexDO indexDO:page.getRecords()){
                executor.submit(()->{
                    try{
                        EnterprisePO enterprisePO = getByUnicode(indexDO.getUniCode());
                        if(enterprisePO == null){
                            updateByUnicode(indexDO.getUniCode());
                            return;
                        }
                        enterprisePO.setGrowthIndex(new Double(indexDO.getGrowthIndex().toString()));
                        enterprisePO.setExpansionIndex(new Double(indexDO.getExpansionIndex().toString()));
                        elasticsearchRestTemplate.save(enterprisePO,indexCoordinates);
                        log.info("同步企业指标信息中，{}/{}",cnt.incrementAndGet(),total);
                    }catch (Exception e){
                        log.error("同步企业异常：",e);
                    }
                });
            }
        }
        return RPCResultBuilder.build(RPCStatus.SUCCESS, "success");
    }

    @Override
    public RPCResult<String> updateDevelopmentIndexByUnicode(String uniCode) {
        log.info("开始同步企业指标信息，企业UniCode为：{}", uniCode);
        EnterprisePO enterprisePO = getByUnicode(uniCode);
        if(enterprisePO == null){
            return updateByUnicode(uniCode);
        }
        EnterpriseDevelopmentIndexDO developmentIndexDO = enterpriseDevelopmentIndexMapper.selectByUniCode(uniCode);
        if(developmentIndexDO == null){
            return RPCResultBuilder.build(RPCStatus.SYSTEM_UNEXCEPT_ERROR, "未找到指标数据");
        }
        enterprisePO.setGrowthIndex(developmentIndexDO.getGrowthIndex().doubleValue());
        enterprisePO.setExpansionIndex(developmentIndexDO.getExpansionIndex().doubleValue());
        elasticsearchRestTemplate.save(enterprisePO,elasticsearchRestTemplate.getIndexCoordinatesFor(EnterprisePO.class));
        return RPCResultBuilder.build(RPCStatus.SUCCESS, "success");
    }

    @Override
    public RPCResult<String> updateAllAncestor() {
        log.info("开始同步企业企业籍贯信息");
        IndexCoordinates indexCoordinates = elasticsearchRestTemplate.getIndexCoordinatesFor(EnterprisePO.class);
        IPage<EnterpriseCodesDTO> page = enterpriseAncestorMapper.selectByPage(Page.of(1,BATCH_SIZE));
        AtomicInteger cnt = new AtomicInteger(0);
        for (int i=1;i<=page.getPages();i++){
            page=enterpriseAncestorMapper.selectByPage(Page.of(i,BATCH_SIZE));
            long total = page.getTotal();
            for(EnterpriseCodesDTO codesDTO:page.getRecords()){
                executor.submit(()->{
                    try{
                        EnterprisePO enterprisePO = getByUnicode(codesDTO.getUniCode());
                        if(enterprisePO == null){
                            updateByUnicode(codesDTO.getUniCode());
                            return;
                        }
                        List<String> codes = Arrays.stream(codesDTO.getCodes().split("/")).collect(Collectors.toList());
                        Set<String> tmpSet = new HashSet<>();
                        codes.forEach(code->{
                            tmpSet.add(code);
                            tmpSet.add(code.substring(0,2)+"0000");
                            tmpSet.add(code.substring(0,4)+"00");
                        });
                        enterprisePO.setAncestorCode(new ArrayList<>(tmpSet));
                        elasticsearchRestTemplate.save(enterprisePO,indexCoordinates);
//                        //同步到知识库ES
//                        knowledgeSyncContext.executeSync(KnowledgeStrategyEnum.ENTERPRISE_SYNC_STRATEGY, enterprisePO);

                        log.info("同步企业籍贯信息中，{}/{}",cnt.incrementAndGet(),total);
                    }catch (Exception e){
                        log.error("同步企业异常：",e);
                    }
                });
            }
        }
        return RPCResultBuilder.build(RPCStatus.SUCCESS, "success");
    }

    @Override
    public RPCResult<String> updateAllAcademia() {
        log.info("开始同步企业企业校友信息");
        IndexCoordinates indexCoordinates = elasticsearchRestTemplate.getIndexCoordinatesFor(EnterprisePO.class);
        IPage<EnterpriseCodesDTO> page = enterpriseAcademiaMapper.selectByPage(Page.of(1,BATCH_SIZE));
        AtomicInteger cnt = new AtomicInteger(0);
        for (int i=1;i<=page.getPages();i++){
            page=enterpriseAcademiaMapper.selectByPage(Page.of(i,BATCH_SIZE));
            long total = page.getTotal();
            for(EnterpriseCodesDTO codesDTO:page.getRecords()){
                executor.submit(()->{
                    try{
                        EnterprisePO enterprisePO = getByUnicode(codesDTO.getUniCode());
                        if(enterprisePO == null){
                            updateByUnicode(codesDTO.getUniCode());
                            return;
                        }
                        List<String> codes = Arrays.stream(codesDTO.getCodes().split("/")).collect(Collectors.toList());
                        List<String> academiaMd5s = Arrays.stream(codesDTO.getOthers().split("/")).collect(Collectors.toList());
                        Set<String> tmpSet = new HashSet<>();
                        codes.forEach(code->{
                            tmpSet.add(code);
                            tmpSet.add(code.substring(0,2)+"0000");
                            tmpSet.add(code.substring(0,4)+"00");
                        });
                        enterprisePO.setAcademicCode(new ArrayList<>(tmpSet));
                        enterprisePO.setAcademicMd5s(new ArrayList<>(new HashSet<>(academiaMd5s)));
                        elasticsearchRestTemplate.save(enterprisePO,indexCoordinates);
//                        //同步到知识库ES
//                        knowledgeSyncContext.executeSync(KnowledgeStrategyEnum.ENTERPRISE_SYNC_STRATEGY, enterprisePO);
                        log.info("同步企业学校信息中，{}/{}",cnt.incrementAndGet(),total);
                    }catch (Exception e){
                        log.error("同步企业异常：",e);
                    }
                });
            }
        }
        return RPCResultBuilder.build(RPCStatus.SUCCESS, "success");
    }

    private EnterprisePO getByUnicode(String uniCode){
        BoolQueryBuilder boolQuery = boolQuery();
        boolQuery.must(QueryBuilders.termsQuery("unifiedSocialCreditCode", Collections.singleton(uniCode)));
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        List<EnterprisePO> searchHits = elasticsearchRestTemplate.search(build, EnterprisePO.class).getSearchHits().stream().map(e->e.getContent()).collect(Collectors.toList());
        if (searchHits.isEmpty()){
            return null;
        }
        return searchHits.get(0);
    }
}
