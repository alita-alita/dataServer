package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 游离企业产品表
 * </p>
 *
 * @author MengDa
 * @since 2025-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_product_matches_dissociated")
public class RecordProductMatchesDissociatedDO extends BaseDO {

	private Long chainId;

	private String enterpriseId;

	private String enterpriseName;

	private String enterpriseUniCode;

	private String productId;

	private String productName;

	private String productUrl;

	private String productDescription;

	private String productPurpose;

	private Long labelId;

	private String labelName;

	private Long nodeId;

	private String nodeName;

	private BigDecimal matchedScore;

	private String matchReason;

	/**
	 * 0 未处理 1 已处理 2 忽略
	 */
	private Integer status;

	private String dataSource;
}
