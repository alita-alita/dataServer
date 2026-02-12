package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 海外企业上市表
 * </p>
 *
 * @author MengDa
 * @since 2025-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_enterprise_stock")
public class OverseasEnterpriseStockDO extends DataSyncBaseDO {

	/**
	 * 企业id
	 */
	private String enterpriseMd5;

	/**
	 * 上市公司简称
	 */
	private String stockShortname;

	/**
	 * 上市公司名称
	 */
	private String stockName;

	/**
	 * 股票代码
	 */
	private String stockCode;

	/**
	 * 上市板块
	 */
	private String stockPlate;

	/**
	 * 上市时间
	 */
	private LocalDate listDate;

	/**
	 * 退市时间
	 */
	private LocalDate delistDate;

	/**
	 * 证券类型
	 */
	private String securityType;

	/**
	 * 上市市场
	 */
	private String stockMarket;
}
