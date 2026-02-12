package cn.idicc.taotie.infrastructment.enums;

import lombok.Getter;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description: 招商资讯发布状态枚举
 * @version: 1.0
 */
@Getter
public enum ReleaseStatusEnum {

    TO_BE_PROCESSED( 0, "待处理"),

    PUBLISHED( 1, "已发布"),

    NOT_PASSED(2, "未通过"),

    OFFLINE(3, "已下线"),
    ;

    private Integer code;

    private String name;

    ReleaseStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ReleaseStatusEnum getByCode(Integer code) {
        for (ReleaseStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
