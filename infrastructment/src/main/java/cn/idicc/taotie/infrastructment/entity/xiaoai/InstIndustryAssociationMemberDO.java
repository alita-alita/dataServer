package cn.idicc.taotie.infrastructment.entity.xiaoai;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 行业协会会员企业表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("inst_industry_association_member")
public class InstIndustryAssociationMemberDO extends BaseDO {

	/**
	 * 行业协会id
	 */
	private String industryAssociationId;

	/**
	 * 会员企业名称
	 */
	private String memberName;

	/**
	 * 会员企业统一社会信用代码
	 */
	private String memberUniCode;

}
