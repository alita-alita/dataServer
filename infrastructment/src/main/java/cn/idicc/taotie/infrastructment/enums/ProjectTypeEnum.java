package cn.idicc.taotie.infrastructment.enums;

import lombok.Getter;

/**
 * @Author: WangZi
 * @Date: 2024/3/6
 * @Description:
 * @version: 1.0
 */
@Getter
public enum ProjectTypeEnum {

    HIGH_VALUE(1, "高价值专利培育项目"),
    ENTREPRENEURSHIP(2, "早小创业类公司项目"),
    ;

    private final Integer code;

    private final String desc;

    ProjectTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectTypeEnum getByCode(Integer code) {
        for (ProjectTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
