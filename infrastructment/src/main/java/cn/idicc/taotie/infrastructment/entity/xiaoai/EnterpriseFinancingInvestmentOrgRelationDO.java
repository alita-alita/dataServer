package cn.idicc.taotie.infrastructment.entity.xiaoai;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_financing_investment_org_relation")
public class EnterpriseFinancingInvestmentOrgRelationDO extends BaseDO {

	/**
	 * 机构统一社会信用代码
	 */
	private String organizationUniCode;

	/**
	 * 企业统一社会信用代码
	 */
	private String enterpriseUniCode;

	/**
	 * 融资轮次
	 */
	private String financingRound;

}
