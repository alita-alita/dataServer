package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: WangZi
 * @Date: 2023/3/29
 * @Description: 招商推荐导入model
 * @version: 1.0
 */
@Data
public class InvestmentEnterpriseRecommendUploadDTO implements Serializable {

    private static final long serialVersionUID = -2163702476207128259L;

    @Alias("推荐机构")
    @ExcelProperty("推荐机构")
    private String orgName;

    @Alias("企业名称")
    @ExcelProperty("企业名称")
    private String enterpriseName;

    @Alias("推荐日期")
    @ExcelProperty("推荐日期")
    private String recommendedDate;

    @Alias("招商模式")
    @ExcelProperty("招商模式")
    private String type;

    @Alias("推荐理由详情")
    @ExcelProperty("推荐理由详情")
    private String recommendationReasonDetail;

    @Alias("关联亲商姓名唯一标识")
    @ExcelProperty("关联亲商姓名唯一标识")
    private String relationUserNameOnlyLogo;

    @Alias("亲商模式推荐关联关系")
    @ExcelProperty("亲商模式推荐关联关系")
    private String associationRelationship;

    @Alias("资源需求")
    @ExcelProperty("资源需求")
    private String resourceNeeds;

    @Alias("关联本地企业社会统一信用代码")
    @ExcelProperty("关联本地企业社会统一信用代码")
    private String associateLocalEnterpriseCode;

    @Alias("供应关系")
    @ExcelProperty("供应关系")
    private String supplyRelation;

    @Alias("关联政策")
    @ExcelProperty("关联政策")
    private String associatePolicy;

    @Alias("关联资讯url")
    @ExcelProperty("关联资讯url")
    private String associateInformationUrl;

    @Alias("对外投资意愿")
    @ExcelProperty("对外投资意愿")
    private String outsideInvestSatisfaction;
}
