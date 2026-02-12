package cn.idicc.taotie.infrastructment.request.data;

import lombok.Data;

@Data
public class DownloadTaskRecordsReq {
    //分页
    private Integer pageNum=1;
    private Integer pageSize=10;
    private Integer total;

}
