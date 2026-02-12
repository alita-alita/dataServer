package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 合作交流表
 * </p>
 *
 * @author MengDa
 * @since 2024-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cooperation")
public class CooperationDO extends DataSyncBaseDO {

	/**
	 * 本方id md5
	 */
	private String selfId;

	/**
	 * 本方社会统一信用代码
	 */
	private String selfUniCode;

	/**
	 * 本方类型 0 企业 1 学院 2 机构/政府
	 * <p>
	 * {@link  cn.idicc.wenchang.base.enums.CooperationMemberTypeEnum}
	 */
	private Integer selfType;

	/**
	 * 来访单位id md5
	 */
	private String counterpartId;

	/**
	 * 合作企业统一社会信用代码
	 */
	private String counterpartUniCode;

	/**
	 * 合作方类型 0 企业 1 学院 2 机构/政府
	 * <p>
	 * {@link  cn.idicc.wenchang.base.enums.CooperationMemberTypeEnum}
	 */
	private Integer counterpartType;

	/**
	 * 合作方式(合作交流 0 、战略合作 1、签订合同 2）
	 * <p>
	 * {@link  cn.idicc.wenchang.base.enums.CooperationTypeEnum}
	 */
	private Integer cooperationType;

	/**
	 * 合作日期
	 */
	private Date relateDate;

	/**
	 * 合同名称
	 */
	private String contractName;

	/**
	 * 合作性质
	 */
	private String contractAttribute;

	/**
	 * 合作内容
	 */
	private String content;

	/**
	 * 合作金额（人民币万元）
	 */
	private BigDecimal cooperationAmount;

	/**
	 * 舆情MD5
	 */
	private String newsMd5;
}
