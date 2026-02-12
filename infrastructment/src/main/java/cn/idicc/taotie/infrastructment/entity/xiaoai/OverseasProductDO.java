package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 海外产品基础信息表
 * </p>
 *
 * @author MengDa
 * @since 2025-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_product")
public class OverseasProductDO extends DataSyncBaseDO {

	/**
	 * md5
	 */
	private String productMd5;

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
	private LocalDate publishDate;

	/**
	 * 产品发布日期文本
	 */
	private String publishDateStr;

	/**
	 * 产品描述
	 */
	private String productDescription;

	/**
	 * 应用领域
	 */
	private String applicationField;

	/**
	 * 产品用途
	 */
	private String productPurpose;

	/**
	 * 产品优势
	 */
	private String productAdvantage;

	/**
	 * 市场规划
	 */
	private String productPlanning;

	/**
	 * 企业md5
	 */
	private String enterpriseMd5;

	/**
	 * 舆情文章链接
	 */
	private String newsUrl;

	/**
	 * 舆情md5
	 */
	private String newsMd5;


}
