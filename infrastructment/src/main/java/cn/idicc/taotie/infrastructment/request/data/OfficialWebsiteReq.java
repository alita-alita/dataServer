package cn.idicc.taotie.infrastructment.request.data;

import lombok.Data;
import org.apache.ibatis.annotations.Param;

@Data
public class OfficialWebsiteReq {

     /**
      * 企业名称
      */
     public String officialWebsiteName;
     /**
      * 采集状态（0=未操作  1=发送成功  2=发送失败）
      */
     public Integer officialWebsiteState;

     //分页
     private Integer pageNum=1;
     private Integer pageSize=10;
     private Integer total;

}
