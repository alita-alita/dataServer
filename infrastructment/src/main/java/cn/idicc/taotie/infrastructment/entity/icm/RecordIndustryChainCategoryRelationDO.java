package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 产业链分类关系表
 * @TableName industry_chain_category_relation
 */
@TableName(value ="record_industry_chain_category_relation")
@Data
public class RecordIndustryChainCategoryRelationDO extends BaseDO{
    private static final long serialVersionUID = 1L;


    /**
     * 产业链ID
     */
    private Long chainId;

    /**
     * 分类ID
     */
    private Long categoryId;

    public static RecordIndustryChainCategoryRelationDO adapter(JSONObject jsonObject){
        RecordIndustryChainCategoryRelationDO res = new RecordIndustryChainCategoryRelationDO();
        res.setChainId(jsonObject.getLong("chain_id"));
        res.setCategoryId(jsonObject.getLong("category_id"));
        res.setDeleted(jsonObject.getBoolean("deleted"));
        return res;
    }
}