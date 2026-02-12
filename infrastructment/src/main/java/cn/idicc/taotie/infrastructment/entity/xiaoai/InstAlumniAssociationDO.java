package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inst_alumni_association")
public class InstAlumniAssociationDO extends DataSyncBaseDO {

	/**
	 * 校友会md5
	 */
	private String alumniMd5;

	/**
	 * 校友会名称
	 */
	private String name;

	/**
	 * 校友会对应的官网网址
	 */
	private String url;

	/**
	 * 校友会对应的公众号名称
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
	 * 教育机构id
	 */
	private String academiaMd5;
}
