package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description: 资讯实体类
 * @version: 1.0
 */
@Data
@TableName("information")
public class InformationDO extends BaseDO {

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * url地址
     */
    @TableField("url")
    private String url;

    /**
     * 发布日期
     */
    @TableField("publishing_date")
    private LocalDate publishingDate;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 是否招商资讯 0否1是
     */
    @TableField("is_investment")
    private Boolean isInvestment;

    /**
     * 是否产业资讯 0否1是
     */
    @TableField("is_industry")
    private Boolean isIndustry;

    /**
     * 是否企业资讯 0否1是
     */
    @TableField("is_enterprise")
    private Boolean isEnterprise;

    /**
     * 是否潜在机会 0否1是
     */
    @TableField("is_change")
    private Boolean isChange;
}
