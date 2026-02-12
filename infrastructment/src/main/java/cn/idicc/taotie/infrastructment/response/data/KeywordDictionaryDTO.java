package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.date.DatePattern;
import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.data.KeywordDictionaryDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description:
 * @version: 1.0
 */
@Data
public class KeywordDictionaryDTO implements Serializable {

    private static final long serialVersionUID = 86633765140556186L;

    private Long id;

    /**
     * 关键字名称
     */
    private String name;

    /**
     * 关键字类型,具体查看KeywordTypeEnum
     */
    private Integer keywordType;

    private Boolean deleted;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtCreate;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtModify;

    public static KeywordDictionaryDTO adapt(KeywordDictionaryDO param) {
        return BeanUtil.copyProperties(param, KeywordDictionaryDTO.class);
    }
}
