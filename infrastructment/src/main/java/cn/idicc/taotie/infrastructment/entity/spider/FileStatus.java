package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件状态表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileStatus {

    //文件状态表主键
    private Long fileStatusId;
    //文件关联压缩包-外键
    private Integer fileCompressionId;
    //文件名称
    private String fileName;
    //错误信息（当部分导入成功或导入失败时）
    private String errorInfo;
    // 删除 0:否 1是
    private Integer deleted;

}
