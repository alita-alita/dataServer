package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * <p>
 * 产业链企业疑似名录
 * </p>
 *
 * @author MengDa
 * @since 2025-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_app_enterprise_industry_chain_suspected")
public class RecordAppEnterpriseIndustryChainSuspectedDO extends BaseDO {

	private String bizId;

	/**
	 * 企业id
	 */
	private String enterpriseId;

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业社会统一信用代码
	 */
	private String enterpriseUniCode;

	/**
	 * 产业链id
	 */
	private Long industryChainId;

	/**
	 * 产业链
	 */
	private String industryChainName;

	/**
	 * 疑似企业的检索词
	 */
	private String suspectedClue;

	/**
	 * 数据来源
	 */
	private String dataSource;


	private Integer status;

	private String failReason;

	private Boolean negative;

	public RecordAppEnterpriseIndustryChainSuspectedDO(String bizId, String enterpriseId, String enterpriseName, String enterpriseUniCode,
													   Long industryChainId, String industryChainName, String suspectedClue, Integer hasProduct,
													   String dataSource, Integer status, Boolean negative, String failReason) {

		this.bizId = bizId;
		this.enterpriseId = enterpriseId;
		this.enterpriseName = enterpriseName;
		this.enterpriseUniCode = enterpriseUniCode;
		this.industryChainId = industryChainId;
		this.industryChainName = industryChainName;
		this.suspectedClue = suspectedClue;
		this.dataSource = dataSource;
		this.status = status;
		this.negative = negative;
		this.failReason = failReason;
	}

	public RecordAppEnterpriseIndustryChainSuspectedDO() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RecordAppEnterpriseIndustryChainSuspectedDO that = (RecordAppEnterpriseIndustryChainSuspectedDO) o;
		return Objects.equals(bizId, that.bizId) &&
				Objects.equals(enterpriseId, that.enterpriseId) &&
				Objects.equals(enterpriseName, that.enterpriseName) &&
				Objects.equals(enterpriseUniCode, that.enterpriseUniCode) &&
				Objects.equals(industryChainId, that.industryChainId) &&
				Objects.equals(industryChainName, that.industryChainName) &&
				Objects.equals(suspectedClue, that.suspectedClue) &&
				Objects.equals(dataSource, that.dataSource) &&
				Objects.equals(status, that.status) &&
				Objects.equals(negative, that.negative) &&
				Objects.equals(failReason, that.failReason);
	}


	@Override
	public int hashCode() {
		return Objects.hash(bizId, enterpriseId, enterpriseName, enterpriseUniCode, industryChainId, industryChainName, suspectedClue,
				dataSource, status, negative, failReason);
	}
}
