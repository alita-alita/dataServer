package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_abnormal_operation")
public class EnterpriseAbnormalOperationDO extends DataSyncBaseDO {

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业社会统一信用代码
	 */
	private String enterpriseUniCode;

	/**
	 * 列入日期
	 */
	private Date listDate;

	/**
	 * 作出决定机关
	 */
	private String decisionMakingAuthority;

	/**
	 * 列入异常经营原因
	 */
	private String reasonForAbnormalOperation;


	/**
	 * 数据来源
	 */
	private String dataSource;


}
