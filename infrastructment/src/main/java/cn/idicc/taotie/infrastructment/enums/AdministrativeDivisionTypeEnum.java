package cn.idicc.taotie.infrastructment.enums;

/**
 * @Author: WangZi
 * @Date: 2023/1/4
 * @Description: 查询行政区划列表类型枚举
 * @version: 1.0
 */
public enum AdministrativeDivisionTypeEnum {

    PROVINCE(1, "省"),
    CITY(2, "市"),
    AREA(3, "区县"),
    ;

    private Integer code;

    private String value;

    AdministrativeDivisionTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static AdministrativeDivisionTypeEnum getByCode(Integer code) {
        for (AdministrativeDivisionTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
