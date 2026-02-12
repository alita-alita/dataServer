package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 企业融资表
 * </p>
 *
 * @author MengDa
 * @since 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_financing_record")
public class EnterpriseFinancingRecordDO extends DataSyncBaseDO {

	private static final long serialVersionUID = -4866777677835580773L;

	/**
	 * 企业统一社会信用代码
	 */
	private String enterpriseUniCode;


	private Date financingDate;


	private String financingRound;


	private BigDecimal financingAmount;


	private String investmentOrganization;
}
