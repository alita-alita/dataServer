package cn.idicc.taotie.infrastructment.po.knowledge;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * @Author: wangjun
 * @Date: 2025/03/27
 * @Description:
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "enterprise_product", createIndex = false)
public class EnterpriseProductPO  {
    /**
     * 定义的是es中的主键id
     */
    @Id
    private String id;

    private Long industryChainId;

    private Long industryChainNodeId;
    /**
     * md5
     */
    @TableField("product_md5")
    private String productMd5;

    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 产品编码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 产品型号
     */
    @TableField("product_model")
    private String productModel;

    /**
     * 产品发布日期
     */
    @TableField("publish_date")
    private Date publishDate;

    /**
     * 产品优势
     */
    @TableField("product_advantage")
    private String productAdvantage;


    /**
     * 市场规划
     */
    @TableField("product_planning")
    private String productPlanning;

    /**
     * 企业统一社会信用代码
     */
    @TableField("enterprise_uni_code")
    private String enterpriseUniCode;


    /**
     * 舆情文章链接
     */
    @TableField("news_url")
    private String newsUrl;

    /**
     * 舆情md5
     */
    @TableField("news_md5")
    private String newsMd5;



    /**
     * 是否逻辑删除   0否1是
     */
    private Boolean deleted;

    /**
     * 创建时间时间戳
     */
    private Long gmtCreate;

    /**
     * 更新时间时间戳
     */
    private Long gmtModify;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
}
