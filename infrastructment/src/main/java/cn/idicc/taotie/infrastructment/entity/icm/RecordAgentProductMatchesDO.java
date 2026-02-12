package cn.idicc.taotie.infrastructment.entity.icm;

import java.math.BigDecimal;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("record_agent_product_matches")
public class RecordAgentProductMatchesDO extends BaseDO {
    /**
     * md5(product_id + industry_chain_id + file_id)
     */
    private String bizId;

    private Long industryChainId;

    private String productId;

    private String productName;

    private String matchedProduct;

    private BigDecimal matchedProductScore;

    private String isProductPurpose;

    /**
     * 疑似企业的检索词
     */
    private String suspectedClue;

    /**
     * 1执行中 2执行完成 3执行失败
     */
    private Integer status;

    private String failReason;


    private String extraReason;

}
