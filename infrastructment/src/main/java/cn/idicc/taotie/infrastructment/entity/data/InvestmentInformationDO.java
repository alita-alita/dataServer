package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description: 招商资讯实体类
 * @version: 1.0
 */
@Data
@TableName("investment_information")
public class InvestmentInformationDO extends BaseDO {

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 链接地址
     */
    @TableField("url")
    private String url;

    /**
     * 发布日期
     */
    @TableField("release_date")
    private LocalDateTime releaseDate;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 关联产业
     */
    @TableField("correlation_industry")
    private String correlationIndustry;

    /**
     * 关联企业
     */
    @TableField("correlation_enterprises")
    private String correlationEnterprises;

    /**
     * 新闻主题，多个使用分号拼接
     */
    @TableField("news_theme")
    private String newsTheme;

    /**
     * 新闻主题id集合，多个使用分号拼接
     */
    @TableField("news_theme_ids")
    private String newsThemeIds;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 发布状态 0待处理 1已发布 2未通过 3已下线
     */
    @TableField("release_status")
    private Integer releaseStatus;
}
