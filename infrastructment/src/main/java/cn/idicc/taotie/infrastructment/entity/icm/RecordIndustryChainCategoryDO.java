package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 产业链分类表
 * @TableName industry_chain_category
 */
@TableName(value ="record_industry_chain_category")
@Data
public class RecordIndustryChainCategoryDO extends BaseDO{
    private static final long serialVersionUID = 1L;

    /**
     * 产业链分类名称
     */
    private String categoryName;

    /**
     * 产业链分类icon
     */
    private String icon;

    private Long parentId;

    private String parentName;

    public static RecordIndustryChainCategoryDO adapter(JSONObject jsonObject){
        RecordIndustryChainCategoryDO res = new RecordIndustryChainCategoryDO();
        res.setId(jsonObject.getLong("id"));
        res.setCategoryName(jsonObject.getString("category_name").isEmpty()?null:jsonObject.getString("category_name"));
        res.setIcon(jsonObject.getString("icon").isEmpty()?null:jsonObject.getString("icon"));
        res.setParentId(jsonObject.getLong("parent_id"));
        res.setParentName(jsonObject.getString("parent_name").isEmpty()?null:jsonObject.getString("parent_name"));
        res.setDeleted(jsonObject.getBoolean("deleted"));
        return res;
    }
}