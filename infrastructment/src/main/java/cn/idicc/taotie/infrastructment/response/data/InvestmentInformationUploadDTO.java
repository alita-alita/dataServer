package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.date.DatePattern;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentInformationDO;
import cn.idicc.taotie.infrastructment.enums.ReleaseStatusEnum;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: WangZi
 * @Date: 2023/3/29
 * @Description: 招商资讯导入模板
 * @version: 1.0
 */
@Data
public class InvestmentInformationUploadDTO implements Serializable {

    private static final long serialVersionUID = -3772090926613995010L;

    @Alias("新闻标题")
    @ExcelProperty("新闻标题")
    private String title;

    @Alias("链接")
    @ExcelProperty("链接")
    private String url;

    @Alias("发布日期")
    @ExcelProperty("发布日期")
    private String releaseDate;

    @Alias("来源")
    @ExcelProperty("来源")
    private String source;

    @Alias("关联产业")
    @ExcelProperty("关联产业")
    private String correlationIndustry;

    @Alias("关联企业")
    @ExcelProperty("关联企业")
    private String correlationEnterprise;

    @Alias("新闻主题")
    @ExcelProperty("新闻主题")
    private String newsTheme;

    @Alias("备注")
    @ExcelProperty("备注")
    private String remark;

    public static InvestmentInformationDO adapt(InvestmentInformationUploadDTO param){
        InvestmentInformationDO result = new InvestmentInformationDO();
        result.setTitle(param.getTitle());
        result.setUrl(param.getUrl());
        result.setSource(param.getSource());
        result.setReleaseDate(DateUtil.Str2LocalDateTime(param.getReleaseDate(), DatePattern.NORM_DATETIME_PATTERN));
        result.setCorrelationIndustry(param.getCorrelationIndustry());
        result.setCorrelationEnterprises(param.correlationEnterprise);
        result.setNewsTheme(param.getNewsTheme());
        result.setRemark(param.getRemark());
        result.setReleaseStatus(ReleaseStatusEnum.PUBLISHED.getCode());
        return result;
    }
}
