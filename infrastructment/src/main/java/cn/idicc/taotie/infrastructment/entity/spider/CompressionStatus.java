package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 压缩包状态表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompressionStatus {
    //压缩包状态表主键
    private Long compressionStatusId;
    //压缩包状态（0进行中、1导入成功、2导入失败）
    private Integer packageStatus;
    //文件状态(0处理中、1全部导入成功、2部分导入成功)
    private Integer fileStatus;
    //对应集市模块所属类型（荣誉资质、股东信息、对外投资、分支机构、融资信息、主要人员、客户、供应商、限制高消费）
    private String dataMarket;
    //处理时间
    private String processingTime;
    //完成时间
    private String completionTime;
    //删除 0:否 1是
    private Integer deleted;


}
