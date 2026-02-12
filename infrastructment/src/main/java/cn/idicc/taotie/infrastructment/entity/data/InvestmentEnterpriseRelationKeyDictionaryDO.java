package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: WangZi
 * @Date: 2023/5/8
 * @Description: 招商企业推荐记录和推荐理由绑定关系实体类
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("investment_enterprise_relation_key_dictionary")
public class InvestmentEnterpriseRelationKeyDictionaryDO extends BaseDO {

    /**
     * 招商企业推荐记录id
     */
    @TableField("investment_enterprise_recommend_id")
    private Long investmentEnterpriseRecommendId;

    /**
     * 推荐理由id
     */
    @TableField("keyword_dictionary_id")
    private Long keywordDictionaryId;
}
