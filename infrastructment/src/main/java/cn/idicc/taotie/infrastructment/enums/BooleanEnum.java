package cn.idicc.taotie.infrastructment.enums;

import lombok.Getter;

/**
 * 布尔类型枚举
 * @author wd
 * @date 2022-12-20
 */
@Getter
public enum BooleanEnum {

    NO(0,"否"),

    YES(1,"是");


    private Integer code;

    private String name;

    BooleanEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }


}
