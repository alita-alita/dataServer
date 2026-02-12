package cn.idicc.taotie.infrastructment.response.icm;

import cn.idicc.common.model.BaseDO;
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
public class RecordProductMatchesAiCheckDTO{

    private Long id;

    private String version;

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


    private String labelDesc;

    private String nodeDesc;

    private Boolean isNegative;
}
