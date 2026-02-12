package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAppEnterpriseIndustryChainSuspectedDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordBlacklistKeywordsDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.spider.CollectTaskRecords;
import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAppEnterpriseIndustryChainSuspectedMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordBlacklistKeywordsMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryChainMapper;
import cn.idicc.taotie.infrastructment.request.icm.RecordAppEnterpriseIndustryChainSuspectedReq;
import cn.idicc.taotie.infrastructment.response.data.ExcelAdoptDTO;
import cn.idicc.taotie.infrastructment.response.icm.RecordAppExcelAdoptDTO;
import cn.idicc.taotie.infrastructment.utils.ExcelUtils;
import cn.idicc.taotie.infrastructment.utils.MD5Util;
import cn.idicc.taotie.service.lucene.LuceneSearcher;
import cn.idicc.taotie.service.services.icm.RecordAppEnterpriseIndustryChainSuspectedService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.xdevapi.InsertResult;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class RecordAppEnterpriseIndustryChainSuspectedServiceImpl implements RecordAppEnterpriseIndustryChainSuspectedService {
    @Autowired
    private RecordAppEnterpriseIndustryChainSuspectedMapper recordAppEnterpriseIndustryChainSuspectedMapper;
    @Autowired
    private RecordBlacklistKeywordsMapper recordBlacklistKeywordsMapper;

    @Autowired
    private RecordIndustryChainMapper industryChainMapper;

    @Autowired
    private LuceneSearcher luceneSearcher;
    @Override
    public PageInfo<RecordAppEnterpriseIndustryChainSuspectedDO> selectAll(RecordAppEnterpriseIndustryChainSuspectedReq recordAppEnterpriseIndustryChainSuspectedReq) {
        PageHelper.startPage(recordAppEnterpriseIndustryChainSuspectedReq.getPageNum(),recordAppEnterpriseIndustryChainSuspectedReq.getPageSize());
        if (recordAppEnterpriseIndustryChainSuspectedReq.getEnterpriseName() == null || recordAppEnterpriseIndustryChainSuspectedReq.getEnterpriseName().equals("")){
                List<RecordAppEnterpriseIndustryChainSuspectedDO> recordAppEnterpriseIndustryChainSuspectedDOList =
                        recordAppEnterpriseIndustryChainSuspectedMapper.selectAll(
                                recordAppEnterpriseIndustryChainSuspectedReq.getEnterpriseName(),
                                recordAppEnterpriseIndustryChainSuspectedReq.getIndustryChainId());
                PageInfo<RecordAppEnterpriseIndustryChainSuspectedDO> pageInfo =
                        new PageInfo<>(recordAppEnterpriseIndustryChainSuspectedDOList);
                return pageInfo;
        }
        try {
            List<RecordAppEnterpriseIndustryChainSuspectedDO> recordAppEnterpriseIndustryChainSuspectedDOList =
                    luceneSearcher.fuzzySearch(
                            recordAppEnterpriseIndustryChainSuspectedReq.getEnterpriseName(),
                            recordAppEnterpriseIndustryChainSuspectedReq.getIndustryChainId());
            PageInfo<RecordAppEnterpriseIndustryChainSuspectedDO> pageInfo = new PageInfo<>(recordAppEnterpriseIndustryChainSuspectedDOList);
            return pageInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // 定义批次大小
    private static final int BATCH_SIZE = 5000;
    @Override
    public Integer addBatchRecordAppEnterpriseIndustryChainSuspected(List<RecordAppEnterpriseIndustryChainSuspectedDO> recordAppEnterpriseIndustryChainSuspectedDOList) {
        int successCount = 0;
        // 检查输入参数是否为空
        if (recordAppEnterpriseIndustryChainSuspectedDOList == null || recordAppEnterpriseIndustryChainSuspectedDOList.isEmpty()) {
            log.error("疑似企业: Input list is empty or null, no records to insert.");
            return 0; // 或者根据业务需求返回其他值或抛出异常
        }
        // 用于存储错误行号
        List<Integer> errorRows = new ArrayList<>();
        int size = recordAppEnterpriseIndustryChainSuspectedDOList.size();
        for (int i = 0; i < size; i += BATCH_SIZE) {
            // 计算本批次的结束索引
            int endIndex = Math.min(i + BATCH_SIZE, size);
            List<RecordAppEnterpriseIndustryChainSuspectedDO> subList = recordAppEnterpriseIndustryChainSuspectedDOList.subList(i, endIndex);
            try {
                // 执行本批次的批量插入操作
                Integer addBatch = recordAppEnterpriseIndustryChainSuspectedMapper.addBatchRecordAppEnterpriseIndustryChainSuspected(subList);
                successCount += addBatch;//累加成功插入的记录数
                log.info("疑似企业: 导入 {}/{}",(i+BATCH_SIZE)/BATCH_SIZE,(size-1+BATCH_SIZE)/BATCH_SIZE);
            } catch (Exception e) {
                // 出现异常，记录错误行号
                for (int j = i; j < endIndex; j++) {
                    errorRows.add(j);
                }
                // 可以在此处添加日志输出，输出错误信息
                log.error(String.format("疑似企业: 导入 %d/%d 疑似企业入库错误：", (i+BATCH_SIZE)/BATCH_SIZE,(size-1+BATCH_SIZE)/BATCH_SIZE),e);
            }
        }
        // 可以在此处将错误行号保存到数据库或日志中，方便后续查询
        if (!errorRows.isEmpty()) {
            log.error("疑似企业: Error rows: {}",errorRows);
        }
        return successCount;
    }

    @Autowired
    private RecordAppEnterpriseIndustryChainSuspectedService recordAppEnterpriseIndustryChainSuspectedService;

    @Override
    public Integer deleteRecordAppEnterpriseIndustryChainSuspected(RecordAppEnterpriseIndustryChainSuspectedDO recordAppEnterpriseIndustryChainSuspectedDO) {
        if (recordAppEnterpriseIndustryChainSuspectedDO == null || recordAppEnterpriseIndustryChainSuspectedDO.getBizId() == null) {
            log.warn("Input parameter is null or bizId is null");
            return 0; // 或者抛出自定义异常
        }
        try {
            Integer deletedCount = recordAppEnterpriseIndustryChainSuspectedMapper.deleteRecordAppEnterpriseIndustryChainSuspected(recordAppEnterpriseIndustryChainSuspectedDO.getBizId());
            log.info("Deleted {} records with bizId: {}", deletedCount, recordAppEnterpriseIndustryChainSuspectedDO.getBizId());
            return deletedCount;
        } catch (Exception e) {
            log.error("Failed to delete record with bizId: {}", recordAppEnterpriseIndustryChainSuspectedDO.getBizId(), e);
            throw new RuntimeException("Failed to delete record", e); // 或者抛出自定义异常
        }
    }



    @Override
    public String suspectedEnterprise(MultipartFile file ,RecordAppEnterpriseIndustryChainSuspectedReq recordAppEnterpriseIndustryChainSuspectedReq) {

        List<RecordAppExcelAdoptDTO> adoptDTOList = null;

        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, RecordAppExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }

        Long industryChainId = recordAppEnterpriseIndustryChainSuspectedReq.getIndustryChainId();

        RecordIndustryChainDO chainDO = industryChainMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
                .eq(RecordIndustryChainDO::getBizId,industryChainId)
        );
        //获取产业链关键词黑名单
        List<RecordBlacklistKeywordsDO> recordBlacklistKeywordsDOS = recordBlacklistKeywordsMapper.selectAll(industryChainId.intValue());

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            RecordAppExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            if (StringUtils.isEmpty(excelAdoptDTO.getEnterpriseName())) {
                msgSb.append("企业名称 为空,");
            }else {
                if (recordBlacklistKeywordsDOS.isEmpty()){
                    excelAdoptDTO.setNegative(0);
                }else{
                    for (RecordBlacklistKeywordsDO recordBlacklistKeywordsDO : recordBlacklistKeywordsDOS){
                        boolean isContains = excelAdoptDTO.getEnterpriseName().contains(recordBlacklistKeywordsDO.getBlacklistKeywordsName());
                        if(isContains){
                            excelAdoptDTO.setNegative(1);
                            break;
                        }else{
                            excelAdoptDTO.setNegative(0);
                        }
                    }
                }
            }

            if (StringUtils.isEmpty(excelAdoptDTO.getEnterpriseUniCode())||excelAdoptDTO.getEnterpriseUniCode().equals("-")) {
                msgSb.append("企业社会统一信用代码 为空,");
            }
            if (StringUtils.isEmpty(excelAdoptDTO.getDataSource())){
                msgSb.append("数据类别 为空,");
            }

            if (msgSb.length() != 0) {
                msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                log.error("疑似企业: 数据校验异常: {}",msgSb.toString());
                errorMsgSb.append(msgSb).append("。\n");
            }
        }
        if (errorMsgSb.length() != 0) {
            return errorMsgSb.toString();
        }
        ArrayList<RecordAppEnterpriseIndustryChainSuspectedDO> chainSuspectedDOS = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            RecordAppEnterpriseIndustryChainSuspectedDO industryChainSuspectedDO = new RecordAppEnterpriseIndustryChainSuspectedDO();

            String enterpriseMd5 = MD5Util.getMd5Id(excelAdoptDTO.getEnterpriseUniCode());

            long existsCount = recordAppEnterpriseIndustryChainSuspectedMapper.selectCount(
                    Wrappers.lambdaQuery(RecordAppEnterpriseIndustryChainSuspectedDO.class)
                            .eq(RecordAppEnterpriseIndustryChainSuspectedDO::getEnterpriseId,enterpriseMd5)
                            .eq(RecordAppEnterpriseIndustryChainSuspectedDO::getIndustryChainId,industryChainId)
                            .eq(RecordAppEnterpriseIndustryChainSuspectedDO::getDeleted, false)
            );
            if(existsCount > 0){
                log.warn("疑似企业: 数据已存在,chain id:{}, enterpriseCode:{}", industryChainId, excelAdoptDTO.getEnterpriseUniCode());
                return;
            }

            industryChainSuspectedDO.setEnterpriseName(excelAdoptDTO.getEnterpriseName());
            industryChainSuspectedDO.setEnterpriseUniCode(excelAdoptDTO.getEnterpriseUniCode());
            industryChainSuspectedDO.setDataSource(excelAdoptDTO.getDataSource());
            //补充数据
            industryChainSuspectedDO.setEnterpriseId(enterpriseMd5);
            industryChainSuspectedDO.setIndustryChainId(industryChainId);
            industryChainSuspectedDO.setIndustryChainName(chainDO.getChainName());
            industryChainSuspectedDO.setNegative(excelAdoptDTO.getNegative() == 1);
            chainSuspectedDOS.add(industryChainSuspectedDO);
        });
        if (!chainSuspectedDOS.isEmpty()) {
            log.info("疑似企业: 文件处理成功，开始保存数据");
            Integer total = recordAppEnterpriseIndustryChainSuspectedService.addBatchRecordAppEnterpriseIndustryChainSuspected(chainSuspectedDOS);
            log.info("疑似企业: 存入成功:{}行数据",total);
        }else{
            log.error("疑似企业: 导入数据异常为空");
        }
        return null;
    }

    @Override
    public int setStatusInitByEnterpriseIds(Set<String> enterpriseIds) {
        return recordAppEnterpriseIndustryChainSuspectedMapper.setStatusInitByEnterpriseIds(enterpriseIds);
    }
}
