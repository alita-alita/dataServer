package cn.idicc.taotie.infrastructment.entity.dw;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dwd_industry_kb")
public class DwdIndustryKbDO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 产业知识库标题（产业简介、产业链构成简述、应用领域、发展史等）
     */
    private String title;

    /**
     * 日期，目前数据库中均为null
     */
    private LocalDate kbDate;

    /**
     * 正文
     */
    private String content;

    /**
     * 逻辑删除标志,1:删除 0:未删除
     */
    @TableLogic(value = "0", delval = "1")
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 最近一次更新时间
     */
    private LocalDateTime gmtModify;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 数据来源
     */
    private String dataSource;
}
