package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description: 企业和资讯关联关系实体
 * @version: 1.0
 */
@Data
@TableName("information_correlation_enterprise")
public class InformationCorrelationEnterpriseDO extends BaseDO {

    /**
     * 统一社会信用代码
     */
    @TableField("unified_social_credit_code")
    private String unifiedSocialCreditCode;

    /**
     * 资讯id
     */
    @TableField("information_id")
    private Long informationId;
}
