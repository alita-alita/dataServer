package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 产品表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_app_product")
public class RecordAppProductDO extends BaseDO {

    private String bizId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品型号
     */
    private String productModel;

    /**
     * 产品发布日期
     */
    private Date publishDate;

    /**
     * 产品发布日期文本
     */
    private String publishDateStr;

    /**
     * 产品描述
     */
    private String productDescription;

    /**
     * 产品用途
     */
    private String productPurpose;

    private String productUrl;

    private String productType;

    /**
     * 产品优势
     */
    private String productAdvantage;

    /**
     * 市场规划
     */
    private String productPlanning;

    /**
     * 企业id
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业统一社会信用代码
     */
    private String enterpriseUniCode;

    /**
     * 舆情文章链接
     */
    private String newsUrl;

    /**
     * 舆情id
     */
    private String newsId;

    /**
     * 数据来源
     */
    private String dataSource;

    private Integer status;


}
