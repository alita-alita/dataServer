package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 产品关联表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_product_matches_ai_check")
public class RecordProductMatchesAiCheckDO extends BaseDO {

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

    private String knowledge;

    private String checkReason;

    private Integer status;

}
