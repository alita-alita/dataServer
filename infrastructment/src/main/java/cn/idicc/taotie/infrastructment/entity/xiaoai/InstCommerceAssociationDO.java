package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 异地商会名录
 * </p>
 *
 * @author MengDa
 * @since 2024-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inst_commerce_association")
public class InstCommerceAssociationDO extends DataSyncBaseDO {

	/**
	 * 商会md5
	 */
	private String commerceMd5;

	/**
	 * 商会名称
	 */
	private String commerceAssociationName;

	/**
	 * 商会对应的官网网址
	 */
	private String url;

	/**
	 * 商会对应的公众号名称
	 */
	private String officialAccount;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 区县
	 */
	private String area;

	/**
	 * 区域代码
	 */
	private String regionCode;

	/**
	 * 商会注册地址
	 */
	private String registerAddress;

	/**
	 * 籍贯省份
	 */
	private String ancestorHomeProvince;

	/**
	 * 籍贯城市
	 */
	private String ancestorHomeCity;

	/**
	 * 籍贯区县
	 */
	private String ancestorHomeArea;

	/**
	 * 籍贯区域代码
	 */
	private String ancestorHomeRegionCode;

	/**
	 * 成立日期
	 */
	private LocalDate registerDate;

	/**
	 * 商会成员数量，非负整数
	 */
	private Integer membershipNumber;


}
