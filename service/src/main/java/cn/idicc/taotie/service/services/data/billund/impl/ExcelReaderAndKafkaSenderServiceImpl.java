package cn.idicc.taotie.service.services.data.billund.impl;

import cn.idicc.taotie.infrastructment.entity.spider.*;
import cn.idicc.taotie.infrastructment.mapper.spider.HoldMapper;
import cn.idicc.taotie.infrastructment.mapper.spider.KeywordMapper;
import cn.idicc.taotie.infrastructment.mapper.spider.OfficialWebsiteMapper;
import cn.idicc.taotie.infrastructment.response.data.ExcelAdoptDTO;
import cn.idicc.taotie.infrastructment.utils.ExcelUtils;
import cn.idicc.taotie.service.config.ExcelKafkaConfig;
import cn.idicc.taotie.service.message.data.ExcelData;
import cn.idicc.taotie.service.services.data.billund.ExcelReaderAndKafkaSenderService;
import cn.idicc.taotie.service.services.data.taoti.ConllectTaskRecordsService;
import cn.idicc.taotie.service.services.data.taoti.HoldService;
import cn.idicc.taotie.service.services.data.taoti.KeywordService;
import cn.idicc.taotie.service.services.data.taoti.OfficialWebsiteService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Configuration
public class ExcelReaderAndKafkaSenderServiceImpl implements ExcelReaderAndKafkaSenderService {

    @Autowired
    private  KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ExcelKafkaConfig excelKafkaConfig;
    @Autowired
    private ConllectTaskRecordsService conllectTaskRecordsService;
    @Autowired
    private KeywordService keywordService;
    @Autowired
    private KeywordMapper keywordMapper;
    @Autowired
    private OfficialWebsiteMapper officialWebsiteMapper;
    @Autowired
    private OfficialWebsiteService officialWebsiteService;
    @Autowired
    private HoldService holdService;
    @Autowired
    private HoldMapper holdMapper;

    @Override
    public String talentInformationCollection(MultipartFile file) {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【人才招商】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(1);

        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
        boolean hasError = false; // 用于标记是否已经出现过任务类型[taskCode]错误

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为 人才招商
            if (excelAdoptDTO.getTaskCode().equals("news_talent")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getCollectType())) {
                    msgSb.append("全量/增量采集 为空,");
                } else {
                    if (!"all".equalsIgnoreCase(excelAdoptDTO.getCollectType()) && !"incre".equalsIgnoreCase(excelAdoptDTO.getCollectType())) {
                        msgSb.append("全量/增量采集 数据格式错误：{}\n");
                    }
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getKeyword())) {
                    msgSb.append("搜索关键词 为空,");
                }   else{
                    boolean isKeywordExists = keywordMapper.isKeywordExists(excelAdoptDTO.getKeyword(),excelAdoptDTO.getPlatform());
                    if (isKeywordExists) {
                        msgSb.append("搜索关键词 重复,");
                    }
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("news_talent")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }

        ArrayList<Keyword> keywordArrayList = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            Keyword keywordData = new Keyword();
            keywordData.setKeywordName(excelAdoptDTO.getKeyword());
            keywordData.setKeywordMode(excelAdoptDTO.getCollectType());
            keywordData.setKeywordPlatform(excelAdoptDTO.getPlatform());
            keywordData.setKeywordTaskCode("news_talent");
            keywordData.setKeywordCycle(excelAdoptDTO.getCycle());
            keywordData.setDistinguish(1);
            keywordArrayList.add(keywordData);
        });

        if (!keywordArrayList.isEmpty()) {
            keywordService.addKeywordBatch(keywordArrayList);
        }
        log.info("关键词保存成功");

        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
        log.info("记录已成功保存至数据库");
        return null;
    }

    @Override
    public String capitalInformationCollection(MultipartFile file) {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【资本招商】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(2);

        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
//        Keyword keywords = keywordMapper.KeywordUniqueCheck();
        boolean hasError = false; // 用于标记是否已经出现过任务类型[taskCode]错误

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为 资本招商
            if (excelAdoptDTO.getTaskCode().equals("news_project")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getCollectType())) {
                    msgSb.append("全量/增量采集 为空,");
                } else {
                    if (!"all".equalsIgnoreCase(excelAdoptDTO.getCollectType()) && !"incre".equalsIgnoreCase(excelAdoptDTO.getCollectType())) {
                        msgSb.append("全量/增量采集 数据格式错误：{}\n");
                    }
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getKeyword())) {
                    msgSb.append("搜索关键词 为空,");
                }
                else{
                  boolean isKeywordExists = keywordMapper.isKeywordExists(excelAdoptDTO.getKeyword(),excelAdoptDTO.getPlatform());
                      if (isKeywordExists) {
                         msgSb.append("搜索关键词 重复,");
                         }
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("news_project")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }
        //把关键字保存到关键字表
        ArrayList<Keyword> keywordArrayList = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            Keyword keywordData = new Keyword();
            keywordData.setKeywordName(excelAdoptDTO.getKeyword());
            keywordData.setKeywordMode(excelAdoptDTO.getCollectType());
            keywordData.setKeywordPlatform(excelAdoptDTO.getPlatform());
            keywordData.setKeywordTaskCode("news_project");
            keywordData.setKeywordCycle(excelAdoptDTO.getCycle());
            keywordData.setDistinguish(1);
            keywordArrayList.add(keywordData);
        });

        if (!keywordArrayList.isEmpty()) {
            keywordService.addKeywordBatch(keywordArrayList);
        }
        log.info("关键词保存成功");
        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
        log.info("记录已成功保存至数据库");
        return null;
    }

    @Override
    public String ProductInformationCollection(MultipartFile file) {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【产品舆情】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(4);

        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
        boolean hasError = false; // 用于标记是否已经出现过任务类型[taskCode]错误

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为 企业舆情
            if (excelAdoptDTO.getTaskCode().equals("news_product")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getCollectType())) {
                    msgSb.append("全量/增量采集 为空,");
                } else {
                    if (!"all".equalsIgnoreCase(excelAdoptDTO.getCollectType()) && !"incre".equalsIgnoreCase(excelAdoptDTO.getCollectType())) {
                        msgSb.append("全量/增量采集 数据格式错误：{}\n");
                    }
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getKeyword())) {
                    msgSb.append("搜索关键词 为空,");
                }
                else{
                    boolean isKeywordExists = keywordMapper.isKeywordExists(excelAdoptDTO.getKeyword(),excelAdoptDTO.getPlatform());
                    if (isKeywordExists) {
                        msgSb.append("搜索关键词 重复,");
                    }
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getSearchedIndustryName())){
                    msgSb.append("产业链 为空,");
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("news_product")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }
        //把关键字保存到关键字表
        ArrayList<Keyword> keywordArrayList = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            Keyword keywordData = new Keyword();
            keywordData.setKeywordName(excelAdoptDTO.getKeyword());
            keywordData.setKeywordMode(excelAdoptDTO.getCollectType());
            keywordData.setKeywordPlatform(excelAdoptDTO.getPlatform());
            keywordData.setKeywordTaskCode("news_product");
            keywordData.setKeywordIndustryName(excelAdoptDTO.getSearchedIndustryName());
            keywordData.setKeywordCycle(excelAdoptDTO.getCycle());
            keywordData.setDistinguish(1);
            keywordArrayList.add(keywordData);
        });

        if (!keywordArrayList.isEmpty()) {
            keywordService.addKeywordBatch(keywordArrayList);
        }
        log.info("关键词保存成功");
        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
         log.info("记录已成功保存至数据库");
        return null;
    }

    @Override
    public String enterpriseInformationCollection(MultipartFile file) {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【企业舆情-关键词】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(3);


        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
        boolean hasError = false; // 用于标记是否已经出现过错误
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为  企业微信公众号采集消息
            if (excelAdoptDTO.getTaskCode().equals("news_enterprise")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getUniCode())) {
                    msgSb.append("企业统一社会信用码 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getSearchedIndustryName())) {
                    msgSb.append("产业链名称 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getCollectType())) {
                    msgSb.append("全量/增量采集 为空,");
                } else {
                    // 校验 collect_type 的值
                    if (!"all".equalsIgnoreCase(excelAdoptDTO.getCollectType()) && !"incre".equalsIgnoreCase(excelAdoptDTO.getCollectType())) {
                        msgSb.append("全量/增量采集 数据格式错误：{}\n");
                    }
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getKeyword())) {
                    msgSb.append("搜索关键词 为空,");
                }   else{
                    boolean isKeywordExists = keywordMapper.isKeywordExists(excelAdoptDTO.getKeyword(),excelAdoptDTO.getPlatform());
                    if (isKeywordExists) {
                        msgSb.append("搜索关键词 重复,");
                    }
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("news_enterprise")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }
        //把关键字保存到关键字表
        ArrayList<Keyword> keywordArrayList = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            Keyword keywordData = new Keyword();
            keywordData.setKeywordName(excelAdoptDTO.getKeyword());
            keywordData.setKeywordMode(excelAdoptDTO.getCollectType());
            keywordData.setKeywordPlatform(excelAdoptDTO.getPlatform());
            keywordData.setKeywordTaskCode("news_enterprise");
            keywordData.setKeywordUniCode(excelAdoptDTO.getUniCode());
            keywordData.setKeywordIndustryName(excelAdoptDTO.getSearchedIndustryName());
            keywordData.setKeywordCycle(excelAdoptDTO.getCycle());
            keywordData.setDistinguish(1);
            keywordArrayList.add(keywordData);
        });

        if (!keywordArrayList.isEmpty()) {
            keywordService.addKeywordBatch(keywordArrayList);
        }
        log.info("关键词保存成功");
        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
        log.info("记录已成功保存至数据库");
        return null;
    }

    @Override
    public String enterpriseOFInformationCollection(MultipartFile file) {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【企业舆情-公众号】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(5);


        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
        boolean hasError = false; // 用于标记是否已经出现过错误
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为  企业微信公众号采集消息
            if (excelAdoptDTO.getTaskCode().equals("news_enterprise")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getUniCode())) {
                    msgSb.append("企业统一社会信用码 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getPublicAccountUrl())) {
                    msgSb.append("公众号url 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getNewsSource())) {
                    msgSb.append("企业公众号名称 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getCollectType())) {
                    msgSb.append("全量/增量采集 为空,");
                } else {
                    // 校验 collect_type 的值
                    if (!"all".equalsIgnoreCase(excelAdoptDTO.getCollectType()) && !"incre".equalsIgnoreCase(excelAdoptDTO.getCollectType())) {
                        msgSb.append("全量/增量采集 数据格式错误：{}\n");
                    }
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getKeyword())) {
                    msgSb.append("搜索关键词 为空,");
                }   else{
                    boolean isKeywordExists = keywordMapper.isKeywordExists(excelAdoptDTO.getKeyword(),excelAdoptDTO.getPlatform());
                    if (isKeywordExists) {
                        msgSb.append("搜索关键词 重复,");
                    }
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("news_enterprise")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }
        //把关键字保存到关键字表
        ArrayList<Keyword> keywordArrayList = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            Keyword keywordData = new Keyword();
            keywordData.setKeywordName(excelAdoptDTO.getKeyword());
            keywordData.setKeywordMode(excelAdoptDTO.getCollectType());
            keywordData.setKeywordPlatform(excelAdoptDTO.getPlatform());
            keywordData.setKeywordTaskCode("news_enterprise");
            keywordData.setKeywordUniCode(excelAdoptDTO.getUniCode());
            keywordData.setKeywordPublicAccountUrl(excelAdoptDTO.getPublicAccountUrl());
            keywordData.setKeywordNewsSource(excelAdoptDTO.getNewsSource());
            keywordData.setKeywordCycle(excelAdoptDTO.getCycle());
            keywordData.setDistinguish(2);
            keywordArrayList.add(keywordData);
        });

        if (!keywordArrayList.isEmpty()) {
            keywordService.addKeywordBatch(keywordArrayList);
        }
        log.info("关键词保存成功");
        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
        log.info("记录已成功保存至数据库");
        return null;
    }

    @Override
    public String qinshangOFInformationCollection(MultipartFile file) {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【亲缘舆情-公众号】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(6);


        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
        boolean hasError = false; // 用于标记是否已经出现过错误
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为  企业微信公众号采集消息
            if (excelAdoptDTO.getTaskCode().equals("news_qinshang")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getPublicAccountUrl())) {
                    msgSb.append("公众号url 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getNewsSource())) {
                    msgSb.append("企业公众号名称 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getCollectType())) {
                    msgSb.append("全量/增量采集 为空,");
                } else {
                    // 校验 collect_type 的值
                    if (!"all".equalsIgnoreCase(excelAdoptDTO.getCollectType()) && !"incre".equalsIgnoreCase(excelAdoptDTO.getCollectType())) {
                        msgSb.append("全量/增量采集 数据格式错误：{}\n");
                    }
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("news_qinshang")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }
        //把关键字保存到关键字表
        ArrayList<Keyword> keywordArrayList = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            Keyword keywordData = new Keyword();
            keywordData.setKeywordMode(excelAdoptDTO.getCollectType());
            keywordData.setKeywordPlatform(excelAdoptDTO.getPlatform());
            keywordData.setKeywordTaskCode("news_qinshang");
            keywordData.setKeywordPublicAccountUrl(excelAdoptDTO.getPublicAccountUrl());
            keywordData.setKeywordNewsSource(excelAdoptDTO.getNewsSource());
            keywordData.setKeywordCycle(excelAdoptDTO.getCycle());
            keywordData.setDistinguish(2);
            keywordArrayList.add(keywordData);
        });

        if (!keywordArrayList.isEmpty()) {
            keywordService.addKeywordBatch(keywordArrayList);
        }
        log.info("关键词保存成功");
        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
        log.info("记录已成功保存至数据库");
        return null;
    }

    @Override
    public String industryOFInformationCollection(MultipartFile file) {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【产业舆情-公众号】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(7);


        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
        boolean hasError = false; // 用于标记是否已经出现过错误
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为  企业微信公众号采集消息
            if (excelAdoptDTO.getTaskCode().equals("news_industry")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getSearchedIndustryName())) {
                    msgSb.append("产业链名称 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getPublicAccountUrl())) {
                    msgSb.append("公众号url 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getNewsSource())) {
                    msgSb.append("企业公众号名称 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getCollectType())) {
                    msgSb.append("全量/增量采集 为空,");
                } else {
                    // 校验 collect_type 的值
                    if (!"all".equalsIgnoreCase(excelAdoptDTO.getCollectType()) && !"incre".equalsIgnoreCase(excelAdoptDTO.getCollectType())) {
                        msgSb.append("全量/增量采集 数据格式错误：{}\n");
                    }
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("news_industry")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }
        //把关键字保存到关键字表
        ArrayList<Keyword> keywordArrayList = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            Keyword keywordData = new Keyword();
            keywordData.setKeywordMode(excelAdoptDTO.getCollectType());
            keywordData.setKeywordPlatform(excelAdoptDTO.getPlatform());
            keywordData.setKeywordTaskCode("news_industry");
            keywordData.setKeywordIndustryName(excelAdoptDTO.getSearchedIndustryName());
            keywordData.setKeywordPublicAccountUrl(excelAdoptDTO.getPublicAccountUrl());
            keywordData.setKeywordNewsSource(excelAdoptDTO.getNewsSource());
            keywordData.setKeywordCycle(excelAdoptDTO.getCycle());
            keywordData.setDistinguish(2);
            keywordArrayList.add(keywordData);
        });

        if (!keywordArrayList.isEmpty()) {
            keywordService.addKeywordBatch(keywordArrayList);
        }
        log.info("关键词保存成功");
        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
        log.info("记录已成功保存至数据库");
        return null;
    }

    @Override
    public String OfficialWebsiteInformationCollection(MultipartFile file)  {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【官网采集】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(8);


        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
        boolean hasError = false; // 用于标记是否已经出现过错误
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为  官网采集
            if (excelAdoptDTO.getTaskCode().equals("and_product")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getEnterpriseId())) {
                    msgSb.append("[官网]企业ID 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getEnterpriseName())) {
                    msgSb.append("[官网]企业名称 为空,");
                }
                if(StringUtils.isEmpty(excelAdoptDTO.getUniCode())){
                    msgSb.append("企业统一社会信用码 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getPublicAccountUrl())) {
                    msgSb.append("公众号url 为空,");
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("and_product")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }
        //把关键字保存到关键字表
        ArrayList<OfficialWebsite> officialWebsite = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            OfficialWebsite officialWebsiteData = new OfficialWebsite();
            officialWebsiteData.setOfficialWebsiteEnterpriseId(excelAdoptDTO.getEnterpriseId());
            officialWebsiteData.setOfficialWebsiteName(excelAdoptDTO.getEnterpriseName());
            officialWebsiteData.setOfficialWebsiteUniCode(excelAdoptDTO.getUniCode());
            officialWebsiteData.setOfficialWebsiteUrl(excelAdoptDTO.getPublicAccountUrl());
            officialWebsiteData.setOfficialWebsiteTaskCode("and_product");
            officialWebsiteData.setOfficialWebsitePlatform(excelAdoptDTO.getPlatform());
            officialWebsiteData.setOfficialWebsiteCycle(excelAdoptDTO.getCycle());
            officialWebsite.add(officialWebsiteData);
        });

        if (!officialWebsite.isEmpty()) {
            officialWebsiteService.addOfficialWebsiteBatch(officialWebsite);
        }
        log.info("关键词保存成功");
        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
        log.info("记录已成功保存至数据库");
        return null;
    }

    @Override
    public String HoldInformationCollection(MultipartFile file) {
        //采集记录
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            // 处理文件名获取失败的情况，比如返回错误信息等
            return "无法获取文件名";
        }
        // 获取当前时间戳，格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStampStr = now.format(formatter);

        // 使用传递进来的真实文件名 originalFileName 和时间戳生成新的文件名
        String fileNameWithTimeStamp = "【核对】" + file.getOriginalFilename() + "_" + timeStampStr;

        // 创建任务记录对象
        CollectTaskRecords collectTaskRecords = new CollectTaskRecords();
        collectTaskRecords.setCollectTaskRecordsName(fileNameWithTimeStamp);
        collectTaskRecords.setCollectTaskRecordsType(9);


        List<ExcelAdoptDTO> adoptDTOList = null;
        try {
            adoptDTOList = ExcelUtils.readMultipartFile(file, ExcelAdoptDTO.class);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (adoptDTOList.isEmpty()) {
            return "execl解析无数据";
        }
        boolean hasError = false; // 用于标记是否已经出现过错误
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        for (int index = 0; index < adoptDTOList.size(); index++) {
            StringBuilder msgSb = new StringBuilder();
            ExcelAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
            // 校验逻辑
            // 判断任务类型是否为  官网采集
            if (excelAdoptDTO.getTaskCode().equals("news_publisher")) {
                if (StringUtils.isEmpty(excelAdoptDTO.getPlatform())) {
                    msgSb.append("采集平台 为空,");
                }
                if (StringUtils.isEmpty(excelAdoptDTO.getEnterpriseName())) {
                    msgSb.append("企业名称 为空,");
                }
                if(StringUtils.isEmpty(excelAdoptDTO.getUniCode())){
                    msgSb.append("企业统一社会信用码 为空,");
                }
                if (excelAdoptDTO.getCycle()==null){
                    msgSb.append("采集周期 为空,");
                }
                if (msgSb.length() != 0) {
                    msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
                    errorMsgSb.append(msgSb).append("。\n");
                }

            }else if(!excelAdoptDTO.getTaskCode().equals("news_publisher")){
                if(hasError == false){
                    errorMsgSb.setLength(0);
                    errorMsgSb.append("任务类型[taskCode]错误请矫正");
                    hasError = true;
                }
                break;
            }
        }
        if (errorMsgSb.length() != 0) {
            // 处理失败，保存失败记录
            log.error("文件解析失败");
            collectTaskRecords.setCollectTaskRecordsState(0); // 设置为失败状态
            conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
            return errorMsgSb.toString();
        }
        //把关键字保存到关键字表
        ArrayList<Hold> holds = new ArrayList<>();
        adoptDTOList.forEach(excelAdoptDTO -> {
            Hold hold = new Hold();
            hold.setHolderName(excelAdoptDTO.getEnterpriseName());
            hold.setHolderUniCode(excelAdoptDTO.getUniCode());
            hold.setHolderTaskCode("news_publisher");
            hold.setHolderPlatform(excelAdoptDTO.getPlatform());
            hold.setHolderCycle(excelAdoptDTO.getCycle());
            holds.add(hold);
        });

        if (!holds.isEmpty()) {
            holdService.addHoldBatch(holds);
        }
        log.info("关键词保存成功");
        // 处理成功，保存数据
        log.info("文件处理成功，开始保存数据");
        collectTaskRecords.setCollectTaskRecordsState(1); // 设置为成功状态
        conllectTaskRecordsService.addConllectTaskRecords(collectTaskRecords);
        log.info("记录已成功保存至数据库");
        return null;
    }


    //--------------------------------------------------------------------------------------

    @Override
    public String manualGatherTalent(List<Integer> keywordIds) {
        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<KeywordKafka> adoptDTOListTalent = keywordService.manualGather("news_talent", keywordIds,1);

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (adoptDTOListTalent.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }

        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
//        adoptDTOListTalent.forEach(Manual -> kafkaTemplate.send(topic, JSONObject.toJSONString(Manual)));
        adoptDTOListTalent.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < keywordIds.size(); i++) {
            Integer keywordId = keywordIds.get(i);
            keywordMapper.updateKeyword(keywordId, null, "incre", 1, null);
            keywordMapper.updateKeywordTime(keywordId);
        }
        return null;
    }

    @Override
    public String manualGatherCapital(List<Integer> keywordIds) {
        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<KeywordKafka> adoptDTOListCapital = keywordService.manualGather("news_project", keywordIds,1);

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (adoptDTOListCapital.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }

        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
        adoptDTOListCapital.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < keywordIds.size(); i++) {
            Integer keywordId = keywordIds.get(i);
            keywordMapper.updateKeyword(keywordId, null, "incre", 1,null);
            keywordMapper.updateKeywordTime(keywordId);
        }
        return null;
    }

    @Override
    public String manualGatherProducts(List<Integer> keywordIds) {
        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<KeywordKafka> adoptDTOListTalent = keywordService.manualGather("news_product", keywordIds,1);
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (adoptDTOListTalent.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }

        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
        adoptDTOListTalent.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < keywordIds.size(); i++) {
            Integer keywordId = keywordIds.get(i);
            keywordMapper.updateKeyword(keywordId, null, "incre", 1, null);
            keywordMapper.updateKeywordTime(keywordId);
        }
        return null;
    }

    @Override
    public String manualGatherEnterprise(List<Integer> keywordIds) {

        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<KeywordKafka> adoptDTOListEnterprise = keywordService.manualGather("news_enterprise", keywordIds,1);

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (adoptDTOListEnterprise.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }

        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
        adoptDTOListEnterprise.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < keywordIds.size(); i++) {
            Integer keywordId = keywordIds.get(i);
            keywordMapper.updateKeyword(keywordId, null, "incre", 1, null);
            keywordMapper.updateKeywordTime(keywordId);
        }
        return null;
    }

    @Override
    public String manualGatherEnterpriseOF(List<Integer> keywordIds) {
        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<KeywordKafka> adoptDTOListeEnterprise = keywordService.manualGather("news_enterprise", keywordIds,2);

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (adoptDTOListeEnterprise.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }

        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
        adoptDTOListeEnterprise.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < keywordIds.size(); i++) {
            Integer keywordId = keywordIds.get(i);
            keywordMapper.updateKeyword(keywordId, null, "incre", 1, null);
            keywordMapper.updateKeywordTime(keywordId);
        }
        return null;
    }

    @Override
    public String manualGatherQinshangOF(List<Integer> keywordIds) {
        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<KeywordKafka> adoptDTOListQinshang = keywordService.manualGather("news_qinshang", keywordIds,2);

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (adoptDTOListQinshang.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }
        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
        adoptDTOListQinshang.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < keywordIds.size(); i++) {
            Integer keywordId = keywordIds.get(i);
            keywordMapper.updateKeyword(keywordId, null, "incre", 1, null);
            keywordMapper.updateKeywordTime(keywordId);
        }
        return null;
    }

    @Override
    public String manualGatherIndustryOF(List<Integer> keywordIds) {
        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<KeywordKafka> adoptDTOListIndustry = keywordService.manualGather("news_industry", keywordIds,2);

        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (adoptDTOListIndustry.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }

        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
        adoptDTOListIndustry.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < keywordIds.size(); i++) {
            Integer keywordId = keywordIds.get(i);
            keywordMapper.updateKeyword(keywordId, null, "incre", 1, null);
            keywordMapper.updateKeywordTime(keywordId);
        }
        return null;
    }

    @Override
    public String manualGatherOfficialWebsiteIds(List<Long> officialWebsiteIds) {
        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<OfficialWebsiteKafka> adoptDTOListOfficialWebsite = officialWebsiteService.manualGather(officialWebsiteIds);
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (adoptDTOListOfficialWebsite.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }

        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
        adoptDTOListOfficialWebsite.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < officialWebsiteIds.size(); i++) {
            Integer officialWebsiteId = Math.toIntExact(officialWebsiteIds.get(i));
            officialWebsiteMapper.updateOfficialWebsiteTime(officialWebsiteId);
        }
        return null;
    }
    @Override
    public String manualGatherHoldIds(List<Long> holdIds) {
        log.info("手动上传开始解析文件");
        //TODO 查询关键词数据
        List<HoldKafka> holdKafkaList = holdService.manualGather(holdIds);
        // 数据校验
        StringBuilder errorMsgSb = new StringBuilder();
        //数据什么情况下会失败
        if (holdKafkaList.size() == 0) {
            // 处理失败，保存失败记录
            log.error("处理失败");
            return errorMsgSb.toString();
        }

        String topic = excelKafkaConfig.getSpiderTopic();
        log.info("开始发送数据到kafka");
        holdKafkaList.forEach(Manual -> {
            String jsonData = JSONObject.toJSONString(Manual);
            log.info("准备发送到Kafka的数据内容：{}", jsonData);
            kafkaTemplate.send(topic, jsonData);
        });
        log.info(excelKafkaConfig);
        log.info("数据发送成功");
        for (int i = 0; i < holdIds.size(); i++) {
            Integer holdId = Math.toIntExact(holdIds.get(i));
            holdService.updateHoldTime(holdId);
        }
        return null;
    }

//------------------------------------------------------------------------------

@XxlJob("AutomaticTime")
public String AutomaticTime() {
    Date currentTime = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义时间格式，可根据数据库中时间字段实际格式调整
    String currentTimeStr = sdf.format(currentTime);
    List<Keyword> resultList = keywordMapper.findByCollectTime(currentTimeStr);
    if (resultList!= null && !resultList.isEmpty()) {
        log.info("开始执行自动采集任务");
        try {
            //TODO 查询关键词数据，这里调用业务服务层方法获取要发送到Kafka的数据列表
            List<Long> keywordIdList = resultList.stream()
                    .map(Keyword::getKeywordId)
                    .collect(Collectors.toList());
            List<KeywordKafka> adoptDTOList = keywordService.maintenanceAutomatic(keywordIdList);
            // 数据校验
            StringBuilder errorMsgSb = new StringBuilder();
            // 根据实际业务规则判断数据什么情况下会失败，这里需要补充具体的校验逻辑，目前仅示例结构
            if (errorMsgSb.length()!= 0) {
                // 处理失败，保存失败记录
                log.error("文件解析失败");
                XxlJobHelper.handleFail("数据校验失败，任务执行不成功");
                return null;
            }

            String topic = excelKafkaConfig.getSpiderTopic();
            log.info("开始发送数据到kafka");
            adoptDTOList.forEach(Automatic -> {
                String jsonData = JSONObject.toJSONString(Automatic);
                log.info("准备发送到Kafka的数据内容：{}", jsonData);
                kafkaTemplate.send(topic, jsonData);
            }
            );

            log.info("数据发送成功");
            for (KeywordKafka dto : adoptDTOList) {
                Integer keywordId = dto.getUniKey();
                keywordMapper.updateKeyword(keywordId, null, "incre", 1, null);
                keywordMapper.updateKeywordTime(keywordId);
            }
            XxlJobHelper.handleSuccess("任务触发成功，采集及数据发送等相关操作均成功完成");
        } catch (Exception e) {
            log.error("执行自动采集任务及发送数据到Kafka过程中出现异常", e);
            XxlJobHelper.handleFail("任务执行出现异常，详情：" + e.getMessage());
        }
    } else {
        XxlJobHelper.handleFail("当前无采集时间与当前时间一致的记录，无需触发任务");
    }
    return null;
}


@XxlJob("AutomaticTimeGW")
public String AutomaticTimeGW() {
    Date currentTime = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义时间格式，可根据数据库中时间字段实际格式调整
    String currentTimeStr = sdf.format(currentTime);
    List<OfficialWebsite> resultList = officialWebsiteMapper.findByCollectTime(currentTimeStr);
    if (resultList!= null && !resultList.isEmpty()) {
        log.info("开始执行自动采集任务");
        try {
            //TODO 查询关键词数据，这里调用业务服务层方法获取要发送到Kafka的数据列表
            List<Long> keywordIdList = resultList.stream()
                    .map(OfficialWebsite::getOfficialWebsiteId)
                    .collect(Collectors.toList());
            List<OfficialWebsiteKafka> adoptDTOList = officialWebsiteService.manualGather(keywordIdList);
            // 数据校验
            StringBuilder errorMsgSb = new StringBuilder();
            // 根据实际业务规则判断数据什么情况下会失败，这里需要补充具体的校验逻辑，目前仅示例结构
            if (errorMsgSb.length()!= 0) {
                // 处理失败，保存失败记录
                log.error("文件解析失败");
                XxlJobHelper.handleFail("数据校验失败，任务执行不成功");
                return null;
            }

            String topic = excelKafkaConfig.getSpiderTopic();
            log.info("开始发送数据到kafka");
            adoptDTOList.forEach(Automatic -> {
                String jsonData = JSONObject.toJSONString(Automatic);
                log.info("准备发送到Kafka的数据内容：{}", jsonData);
                kafkaTemplate.send(topic, jsonData);
            }
            );

            log.info("数据发送成功");
            for (OfficialWebsiteKafka dto : adoptDTOList) {
                Integer uniKey = dto.getUniKey();
                officialWebsiteMapper.updateOfficialWebsiteTime(uniKey);

            }
            XxlJobHelper.handleSuccess("任务触发成功，采集及数据发送等相关操作均成功完成");
        } catch (Exception e) {
            log.error("执行自动采集任务及发送数据到Kafka过程中出现异常", e);
            XxlJobHelper.handleFail("任务执行出现异常，详情：" + e.getMessage());
        }
    } else {
        XxlJobHelper.handleFail("当前无采集时间与当前时间一致的记录，无需触发任务");
    }
    return null;
}

    @XxlJob("AutomaticTimeHold")
    public String AutomaticTimeHold() {
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义时间格式，可根据数据库中时间字段实际格式调整
        String currentTimeStr = sdf.format(currentTime);
        List<Hold> resultList = holdMapper.findByCollectTime(currentTimeStr);
        if (resultList!= null && !resultList.isEmpty()) {
            log.info("开始执行自动采集任务");
            try {
                //TODO 查询关键词数据，这里调用业务服务层方法获取要发送到Kafka的数据列表
                List<Long> holdIdList = resultList.stream()
                        .map(Hold::getHoldId)
                        .collect(Collectors.toList());
                List<HoldKafka> adoptDTOList = holdService.manualGather(holdIdList);
                // 数据校验
                StringBuilder errorMsgSb = new StringBuilder();
                // 根据实际业务规则判断数据什么情况下会失败，这里需要补充具体的校验逻辑，目前仅示例结构
                if (errorMsgSb.length()!= 0) {
                    // 处理失败，保存失败记录
                    log.error("文件解析失败");
                    XxlJobHelper.handleFail("数据校验失败，任务执行不成功");
                    return null;
                }

                String topic = excelKafkaConfig.getSpiderTopic();
                log.info("开始发送数据到kafka");
                adoptDTOList.forEach(Automatic -> {
                            String jsonData = JSONObject.toJSONString(Automatic);
                            log.info("准备发送到Kafka的数据内容：{}", jsonData);
                            kafkaTemplate.send(topic, jsonData);
                        }
                );

                log.info("数据发送成功");
                for (HoldKafka dto : adoptDTOList) {
                    Integer uniKey = dto.getUniKey();
                    holdMapper.updateHoldTime(uniKey);
                }
                XxlJobHelper.handleSuccess("任务触发成功，采集及数据发送等相关操作均成功完成");
            } catch (Exception e) {
                log.error("执行自动采集任务及发送数据到Kafka过程中出现异常", e);
                XxlJobHelper.handleFail("任务执行出现异常，详情：" + e.getMessage());
            }
        } else {
            XxlJobHelper.handleFail("当前无采集时间与当前时间一致的记录，无需触发任务");
        }
        return null;
    }


    //----------------------------------------------------------------
    class ExcelDataListener extends AnalysisEventListener {
        private String originalFileName;
        private List<ExcelData> excelDataList = new ArrayList<>();


        private Map<String, String> titleMap = new HashMap<String, String>() {{
            put("平台", "platform");
            put("社会信用代码", "uniCode");
            put("产业链名称", "industryName");
            put("全量/增量采集", "collectType");
            put("搜索关键字", "keyword");
        }};

        private Map<Integer, String> orderTitleMap = new HashMap<Integer, String>() {{
            put(0, "platform");
            put(1, "uniCode");
            put(2, "industryName");
            put(3, "collectType");
            put(4, "keyword");
        }};

//        public String getErrorMsg() {
//            return msgSb.toString() ;
//        }

        public List<ExcelData> getExcelDataList() {
            return excelDataList;
        }

        // 添加构造函数接收文件名参数
        public ExcelDataListener(String originalFileName) {
            this.originalFileName = originalFileName;
        }

        /**
         * 为空表示数据表格没有问题
         * @return
         */
//        public boolean isCheck(){
//            return msgSb.length() != 0;
//        }

        @Override
        public void invoke(Object object, AnalysisContext context) {
            log.info("当前行：" + context.getCurrentRowNum());


        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        }

        private boolean validateData(ExcelData data) {
            // 校验逻辑，例如非空校验等
            if (data.getUniCode() == null || data.getIndustryName() == null || data.getCollectType() == null || data.getKeyword() == null) {
                return false;
            }

            // 校验 collect_type 的值
            if (!"all".equalsIgnoreCase(data.getCollectType()) && !"incre".equalsIgnoreCase(data.getCollectType())) {
                return false;
            }
            return true;
        }
    }

    }
