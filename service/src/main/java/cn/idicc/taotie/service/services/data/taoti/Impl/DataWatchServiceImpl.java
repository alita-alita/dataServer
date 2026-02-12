package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.enums.DataWatchTypeEnum;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.po.data.DataWatchStatPO;
import cn.idicc.taotie.infrastructment.request.data.DataWatchQueryRequest;
import cn.idicc.taotie.infrastructment.response.data.DataWatchDailyCountStatDTO;
import cn.idicc.taotie.service.services.data.taoti.DataWatchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Service
public class DataWatchServiceImpl implements DataWatchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public Map<String, Object> queryTotalCount() {
        HashMap<String, Object> result = new HashMap<>();
        BoolQueryBuilder boolQuery = boolQuery();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolQuery.must(QueryBuilders.termQuery("recordDate", LocalDate.now().format(formatter)));
        // 2. 构建内层BoolQuery：chainId=0 或 regionCode='000000'
        BoolQueryBuilder conditionBool = QueryBuilders.boolQuery()
                // 条件1：chainId=0 查产业链总量
                .should(QueryBuilders.termQuery("chainId", 0L))
                // 条件2：regionCode='000000' 查所有地区总量
                .should(QueryBuilders.termQuery("regionCode", "000000"))
                // 关键：确保should至少匹配1个条件（否则会返回所有今日数据）
                .minimumShouldMatch(1);
        boolQuery.must(conditionBool);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(0, 10000))
                .build();
        List<DataWatchStatPO> dataWatchStatPOS = elasticsearchRestTemplate.search(build, DataWatchStatPO.class).getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        //不同数据类型对应的总量
        Map<Integer, Long> odsDataTypeCountMap = dataWatchStatPOS.stream().collect(Collectors.toMap(DataWatchStatPO::getDataType, DataWatchStatPO::getOdsCount, (e1, e2) -> e2));
        List<DataWatchDailyCountStatDTO> data = new ArrayList<>();

        for (DataWatchTypeEnum dataWatchTypeEnum : DataWatchTypeEnum.values()) {
            DataWatchDailyCountStatDTO dataWatchDailyCountStatDTO = new DataWatchDailyCountStatDTO();
            dataWatchDailyCountStatDTO.setCode(dataWatchTypeEnum.getCode());
            dataWatchDailyCountStatDTO.setName(dataWatchTypeEnum.getName());
            dataWatchDailyCountStatDTO.setValue(odsDataTypeCountMap.getOrDefault(dataWatchTypeEnum.getCode(), 0L).intValue());
            data.add(dataWatchDailyCountStatDTO);
        }
        result.put("data", data);
        List<DataWatchDailyCountStatDTO> chainOptions = Arrays.stream(DataWatchTypeEnum.values()).filter(item -> item.getRelateDataType() == 0).map(item -> {
            DataWatchDailyCountStatDTO chainOption = new DataWatchDailyCountStatDTO();
            chainOption.setCode(item.getCode());
            chainOption.setName(item.getName());
            return chainOption;
        }).collect(Collectors.toList());

        List<DataWatchDailyCountStatDTO> regionOptions = Arrays.stream(DataWatchTypeEnum.values()).filter(item -> item.getRelateDataType() == 1).map(item -> {
            DataWatchDailyCountStatDTO chainOption = new DataWatchDailyCountStatDTO();
            chainOption.setCode(item.getCode());
            chainOption.setName(item.getName());
            return chainOption;
        }).collect(Collectors.toList());
        result.put("data", data);
        result.put("chainOptions", chainOptions);
        result.put("regionOptions", regionOptions);
        return result;
    }

    @Override
    public List<DataWatchDailyCountStatDTO> queryChartData(DataWatchQueryRequest request) {
        if (request.getDataType() == null) {
            throw new BizException("数据类型不能为空");
        }
        if (request.getStartDate() == null) {
            throw new BizException("日期不能为空");
        }
        //构造条件
        BoolQueryBuilder boolQuery = boolQuery();
        //产业链ID
        long chainId = request.getIndustryChainId() == null ? 0 : request.getIndustryChainId();
        //地区编码
        String regionCode = request.getRegionCode() == null ? "000000" : request.getRegionCode();
        //起始日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = request.getStartDate();

        LocalDate today = LocalDate.now();
        if (startDate.isAfter(today) || startDate.isBefore(today.plusDays(-30))) {
            throw new BizException("日期超出统计范围！");
        }
        //图表类型
        if (request.getSource() != null && request.getSource() == 0) {
            boolQuery.must(QueryBuilders.termQuery("chainId", chainId));
        } else {
            boolQuery.must(QueryBuilders.termQuery("regionCode", regionCode));
        }
        boolQuery.must(QueryBuilders.rangeQuery("recordDate").gte(startDate).format("yyyy-MM-dd"));
        boolQuery.must(QueryBuilders.termQuery("dataType", request.getDataType()));
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(0, 10000))
                .build();
        List<SearchHit<DataWatchStatPO>> searchResult = elasticsearchRestTemplate.search(build, DataWatchStatPO.class).getSearchHits();
        Map<String, Long> odsDataTypeCountMap = searchResult.stream().map(SearchHit::getContent)
                .collect(Collectors.toMap(item -> item.getRecordDate().format(formatter), DataWatchStatPO::getOdsCount, (e1, e2) -> e2));
        Map<String, Long> dwdDataTypeCountMap = searchResult.stream().map(SearchHit::getContent)
                .collect(Collectors.toMap(item -> item.getRecordDate().format(formatter), DataWatchStatPO::getDwdCount, (e1, e2) -> e2));
        List<DataWatchDailyCountStatDTO> data = new ArrayList<>();
        while (!startDate.isAfter(today)) {
            String currentDateStr = startDate.format(formatter);
            DataWatchDailyCountStatDTO dataWatchDailyCountStatDTO = new DataWatchDailyCountStatDTO();
            dataWatchDailyCountStatDTO.setCode(request.getDataType());
            dataWatchDailyCountStatDTO.setName(currentDateStr);
            dataWatchDailyCountStatDTO.setOdsCount(odsDataTypeCountMap.getOrDefault(currentDateStr, 0L).intValue());
            dataWatchDailyCountStatDTO.setDwdCount(dwdDataTypeCountMap.getOrDefault(currentDateStr, 0L).intValue());
            data.add(dataWatchDailyCountStatDTO);
            startDate = startDate.plusDays(1);
        }
        return data;
    }
}
