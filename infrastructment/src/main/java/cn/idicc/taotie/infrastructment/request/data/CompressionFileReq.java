package cn.idicc.taotie.infrastructment.request.data;

import lombok.Data;
import org.apache.ibatis.annotations.Param;

@Data
public class CompressionFileReq {

    private Integer packageStatus;
    private Integer fileStatus;
    private String  dataMarket;

    private Integer fileCompressionId;

    //分页
    private Integer pageNum=1;
    private Integer pageSize=10;
    private Integer total;




}
