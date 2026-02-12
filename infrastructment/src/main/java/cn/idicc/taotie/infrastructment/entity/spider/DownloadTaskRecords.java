package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下载任务记录表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadTaskRecords {

   //下载任务主键
   private Long downloadTaskRecordsId;
   //下载任务业务范围
   private String downloadTaskRecordsName;
   //提交时间
   private String submissionTime;
   //下载完成时间
   private String completionTime;
   //下载任务状态
   private Integer downloadTaskRecordsStates;
   //删除 0否 1是
   private Integer deleted;



}
