package cn.idicc.taotie.infrastructment.enums;

import lombok.Getter;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description: 关键字词典类型枚举类
 * @version: 1.0
 */
@Getter
public enum KeywordTypeEnum {

    UNKNOWN( 0, "未知"),

    NEWS_THEME( 1, "招商资讯新闻主题"),

    RECOMMENDATION_REASON(2, "招商情报推荐理由"),

    INFORMATION_SOURCE(3,"舆情来源"),

    PRODUCT_EXPERIENCE(4,"产品体验关键字"),

    CONSULTING_BUSINESS(5,"咨询业务关键字")
    ;

    private Integer code;

    private String name;

    KeywordTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static KeywordTypeEnum getByCode(Integer code) {
        for (KeywordTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
