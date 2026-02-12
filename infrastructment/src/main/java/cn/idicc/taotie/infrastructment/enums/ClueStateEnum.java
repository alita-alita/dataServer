package cn.idicc.taotie.infrastructment.enums;

import lombok.Getter;

/**
 * @Author: WangZi
 * @Date: 2023/2/23
 * @Description: 招商线索状态枚举
 * @version: 1.0
 */
@Getter
public enum ClueStateEnum {

    TO_BE_ASSIGNED( 0, "待指派"),

    ASSIGNED( 1, "已指派"),
    ;

    private Integer code;

    private String name;

    ClueStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ClueStateEnum getByCode(Integer code) {
        for (ClueStateEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
