package cn.idicc.taotie.infrastructment.enums;

import lombok.Getter;

/**
 * @Author: WangZi
 * @Date: 2023/6/8
 * @Description:
 * @version: 1.0
 */
@Getter
public enum InvestmentTypeEnum {

    ALL(-1, "全部"),

    ADMIN(0, "后台添加"),

    PRO_BUSINESS(1, "亲商招商"),

    RESOURCE(2, "资源招商"),

    CHAIN_OWNER(3, "链主招商"),

    POLICY(4, "政策招商"),

    INFORMATION(5, "舆情招商"),

    AI(6, "AI+招商"),
    ;

    private Integer code;

    private String name;

    InvestmentTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static InvestmentTypeEnum getByCode(Integer code) {
        for (InvestmentTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
