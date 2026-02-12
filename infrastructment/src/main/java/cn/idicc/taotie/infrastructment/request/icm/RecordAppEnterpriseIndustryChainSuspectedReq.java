package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

@Data
public class RecordAppEnterpriseIndustryChainSuspectedReq {

    private String enterpriseName;
    private Long industryChainId;

    //分页
    private Integer pageNum=1;
    private Integer pageSize=10;
    private Integer total;



}
