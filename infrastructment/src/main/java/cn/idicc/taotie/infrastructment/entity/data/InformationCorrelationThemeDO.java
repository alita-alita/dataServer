package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description: 资讯和主题关联关系实体类
 * @version: 1.0
 */
@Data
@TableName("information_correlation_theme")
public class InformationCorrelationThemeDO extends BaseDO {

    /**
     * 主题类型关键字id
     */
    @TableField("keyword_id")
    private Long keywordId;

    /**
     * 资讯id
     */
    @TableField("information_id")
    private Long informationId;
}
