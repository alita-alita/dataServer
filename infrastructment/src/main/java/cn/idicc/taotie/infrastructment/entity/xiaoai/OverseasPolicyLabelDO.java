package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外政策标签关系表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_policy_label")
public class OverseasPolicyLabelDO extends DataSyncBaseDO {

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 政策md5
	 */
	private String policyMd5;

	/**
	 * 政策标签md5
	 */
	private String policyLabelMd5;

}
