package cn.idicc.taotie.infrastructment.request.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DataWatchQueryRequest {

    /**
     * 产业链ID
     */
    private Long industryChainId;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 0产业链数据，1地区数据
     */
    private Integer source;

    /**
     * 省市区编码
     */
    private String regionCode;
}
