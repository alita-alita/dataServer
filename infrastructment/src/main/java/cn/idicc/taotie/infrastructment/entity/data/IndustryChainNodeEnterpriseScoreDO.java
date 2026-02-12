package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: wd
 * @Date: 2023-03-15
 * @Description:产业链节点企业评分信息实体
 * @version: 1.0
 */
@Data
@TableName("industry_chain_node_enterprise_score")
public class IndustryChainNodeEnterpriseScoreDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /** 产业链ID */
    @TableField("chain_id")
    private Long chainId;
    /** 产业链节点ID */
    @TableField("node_id")
    private Long nodeId;
    /** 企业ID */
    @TableField("enterprise_id")
    private Long enterpriseId;
    @TableField("enterprise_name")
    private String enterpriseName;
    /** 统一社会信用代码 */
    @TableField("unified_social_credit_code")
    private String unifiedSocialCreditCode;
    /** 企业评分 */
    @TableField("enterprise_score")
    private Double enterpriseScore;
    /** 省份 */
    @TableField("province")
    private String province;
    /** 城市 */
    @TableField("city")
    private String city;
    /** 区县 */
    @TableField("area")
    private String area;
    /** 注册日期 */
    @TableField("register_date")
    private String registerDate;

}
