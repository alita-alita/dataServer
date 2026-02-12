package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wd
 * @description 产业链标签节点DO
 */
@TableName("industry_label")
@Data
@NoArgsConstructor
public class IndustryLabelDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 标签名称
     */
    private String labelName;

    /**
     * 标签分类：0未知 1产品 2技术
     */
    private Byte labelType;

//    private Long chainId;

    public IndustryLabelDO(String labelName) {
        this.labelName = labelName;
    }
}
