package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业成长、扩张指数
 * </p>
 *
 * @author MengDa
 * @since 2025-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_development_index")
public class EnterpriseDevelopmentIndexDO extends DataSyncBaseDO {

	/**
	 * 企业社会统一信用代码
	 */
	private String uniCode;

	/**
	 * 成长指数
	 */
	private Float growthIndex;

	/**
	 * 扩张指数
	 */
	private Float expansionIndex;
}
