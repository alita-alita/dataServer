package cn.idicc.taotie.infrastructment.enums;

/**
 * @Author: wd
 * @Date: 2023/4/17
 * @Description:资讯类型
 * @version: 1.0
 */
public enum InfoTypeEnum {


    INVESTMENT_INFORMATION(1, "招商资讯"),

    INDUSTRY_INFORMATION(2, "产业资讯"),

    ENTERPRISE_INFORMATION(3, "企业资讯"),
    POTENTIAL_OPPORTUNITY(4, "潜在机会"),
    ;

    private Integer code;

    private String desc;

    InfoTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static InfoTypeEnum getByCode(String code) {
        for (InfoTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
