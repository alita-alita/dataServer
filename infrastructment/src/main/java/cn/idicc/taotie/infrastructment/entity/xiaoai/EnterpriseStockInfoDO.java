package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 企业股市信息
 * </p>
 *
 * @author MengDa
 * @since 2024-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_stock_info")
public class EnterpriseStockInfoDO extends DataSyncBaseDO {
	/**
	 * 企业社会统一信用代码
	 */
	private String uniCode;

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
	 * 上市市场
	 */
	private String stockMarket;
}
