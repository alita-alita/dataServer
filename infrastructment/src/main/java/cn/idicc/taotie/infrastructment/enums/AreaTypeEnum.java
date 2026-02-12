package cn.idicc.taotie.infrastructment.enums;

/**
 * @Author: wd
 * @Date: 2023/1/4
 * @Description: 区域类型枚举
 * @version: 1.0
 */
public enum AreaTypeEnum {

    ALL( 1, "全国"),

    PROVINCE( 2, "省"),

    CITY(3, "市"),

    AREA(4, "区"),

    ;

    private Integer code;

    private String name;

    AreaTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AreaTypeEnum getByCode(Integer code) {
        for (AreaTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
