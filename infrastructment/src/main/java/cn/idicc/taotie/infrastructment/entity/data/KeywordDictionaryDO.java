package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description: 关键字词典表实体类
 * @version: 1.0
 */
@Data
@TableName("keyword_dictionary")
public class KeywordDictionaryDO extends BaseDO {

    /**
     * 关键字名称
     */
    @TableField("name")
    private String name;

    /**
     * 关键字类型 0未知 1招商资讯新闻主题 2招商情报推荐理由
     */
    @TableField("keyword_type")
    private Integer keywordType;
}
