package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 政府机构表
 * </p>
 *
 * @author MengDa
 * @since 2024-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inst_gov_organization")
public class InstGovOrganizationDO extends DataSyncBaseDO {

	/**
	 * 政府机构名称
	 */
	private String organizationName;

	/**
	 * md5(organization_name)
	 */
	private String ukMd5;

	/**
	 * 政府机构类型
	 */
	private String organizationType;

	/**
	 * 成立日期
	 */
	private Date registerDate;

	/**
	 * 机构层级 0 全国 1 省级 2 市级 3 区县
	 */
	private Integer organizationLevel;

	/**
	 * 对应的官网网址
	 */
	private String url;

	/**
	 * 对应的公众号名称
	 */
	private String officialAccount;

	/**
	 * 区域代码
	 */
	private String regionCode;

	/**
	 * 地址
	 */
	private String address;


}
