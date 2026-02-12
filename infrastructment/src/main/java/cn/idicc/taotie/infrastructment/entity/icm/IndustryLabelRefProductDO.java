package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.taotie.infrastructment.entity.xiaoai.DataSyncBaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("industry_label_ref_product")
public class IndustryLabelRefProductDO extends DataSyncBaseDO {

	@TableField("industry_label_id")
	private Long industryLabelId;
	@TableField("product_id")
	private String productId;

}
