package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wd
 * @description 企业产业链标签关系DO
 */
@TableName("enterprise_industry_label_relation")
@Data
@EqualsAndHashCode(of = {"enterpriseId", "labelId"})
@NoArgsConstructor
public class EnterpriseIndustryLabelRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 产业链标签id
     */
    private Long labelId;

    /**
     * 是否确认 0否1是
     */
    @TableField("confirm")
    private Boolean confirm;

    /**
     * 批次号
     */
    @TableField("batch_number")
    private String batchNumber;

    /**
     * 命中规则 0手动绑定 1专利搜索 2企查查和文章均存在 3挂载类型产业链节点和其父节点的节点名称存在于企业名称中
     */
    @TableField("hit_rule")
    private Integer hitRule;

}
