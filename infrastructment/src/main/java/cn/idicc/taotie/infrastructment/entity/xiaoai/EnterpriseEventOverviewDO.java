package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业事件综述表
 * </p>
 *
 * @author MengDa
 * @since 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_event_overview")
public class EnterpriseEventOverviewDO extends DataSyncBaseDO {

	/**
	 * 企业社会统一信用代码
	 */
	private String uniCode;

	/**
	 * 事件内容
	 */
	private String eventOverview;

}
