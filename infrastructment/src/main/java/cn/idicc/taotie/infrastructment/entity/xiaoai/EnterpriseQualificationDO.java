package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 企业资质/奖项表
 * </p>
 *
 * @author MengDa
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_qualification")
public class EnterpriseQualificationDO extends DataSyncBaseDO {

	/**
	 * 企业统一社会信用代码
	 */
	private String enterpriseUniCode;

	/**
	 * 0 资质 1 奖项
	 */
	private Integer type;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 0 国家级 1 省级 2 市级
	 */
	private Integer divisionLevel;

	/**
	 * 发布日期
	 */
	private Date publishDate;

	/**
	 * 失效日期
	 */
	private Date endDate;
}
