package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业与人才关联表
 * </p>
 *
 * @author MengDa
 * @since 2024-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_talent_relation")
public class EnterpriseTalentRelationDO extends DataSyncBaseDO {
	/**
	 * 企业 0 高校 1
	 */
	private Integer type;

	/**
	 * 高校Md5
	 */
	private String academiaMd5;

	/**
	 * 企业 or 机构 unicode
	 */
	private String uniCode;

	/**
	 * 人才
	 */
	private String talentMd5;

	/**
	 * 职称
	 */
	private String occupation;

	/**
	 * 数据来源
	 */
	private String dataSource;

}
