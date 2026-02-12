package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 企业排行榜表
 * </p>
 *
 * @author MengDa
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_ranking")
public class EnterpriseRankingDO extends DataSyncBaseDO {

	/**
	 * 企业统一社会信用代码
	 */
	private String enterpriseUniCode;

	/**
	 * 认定机构
	 */
	private String certifyingAuthority;

	/**
	 * 榜单名称
	 */
	private String rankingName;

	/**
	 * 榜单名次
	 */
	private Integer rankingSort;

	/**
	 * 榜单年份
	 */
	private String rankingYear;

	/**
	 * 发布日期
	 */
	private Date publishDate;

	/**
	 * 依据指标(营业收入、利润等）
	 */
	private String rankingIndicator;

	/**
	 * 来源
	 */
	private String dataSource;

	/**
	 * 排行榜来源
	 */
	private String rankingSource;
}
