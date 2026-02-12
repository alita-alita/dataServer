package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 黑名单关键词表
 */
@TableName(value ="record_blacklist_keywords")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordBlacklistKeywordsDO extends BaseDO {
    private static final long serialVersionUID = 1L;

    /**黑名单关键字*/
    private String blacklistKeywordsName;
    /**所属产业链*/
    private Integer blacklistIndustrialChain;
}
