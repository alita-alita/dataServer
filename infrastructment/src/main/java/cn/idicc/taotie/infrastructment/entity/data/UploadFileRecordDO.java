package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/3/28
 * @Description: 上传文件记录
 * @version: 1.0
 */
@Data
@TableName("upload_file_record")
public class UploadFileRecordDO extends BaseDO {

    /**
     * 文件名称
     */
    @TableField("name")
    private String name;

    /**
     * 文件路径
     */
    @TableField("path")
    private String path;

    /**
     * 批次号
     */
    @TableField("batch_number")
    private String batchNumber;

    /**
     * 文件类型
     */
    @TableField("type")
    private String type;

    /**
     * 状态  0待处理 1不必处理 2处理成功 3处理失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 处理失败原因
     */
    @TableField("fail_msg")
    private String failMsg;

    /**
     * 处理类型
     */
    @TableField("deal_type")
    private String dealType;
}
