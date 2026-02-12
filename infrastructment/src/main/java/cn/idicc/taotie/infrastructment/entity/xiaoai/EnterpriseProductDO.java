package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 企业融资表
 * </p>
 *
 * @author MengDa
 * @since 2023-05-23
 */
@Data
@TableName("enterprise_product")
public class EnterpriseProductDO extends DataSyncBaseDO {

	private static final long serialVersionUID = -4866777677835580773L;

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
}
