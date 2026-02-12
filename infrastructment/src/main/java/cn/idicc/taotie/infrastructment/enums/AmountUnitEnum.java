package cn.idicc.taotie.infrastructment.enums;

/**
 * @Author: wd
 * @Date: 2023/4/20
 * @Description:
 * @version: 1.0
 */
public enum AmountUnitEnum {

    CNY(1, "人民币"),
    USD(2, "美元"),
    EUR(3, "欧元"),
    JPY(4, "日元"),
    GBP(5, "英镑"),
    CHF(6, "瑞士法郎"),
    CAD(7, "加元"),
    HKD(8, "港元"),
    AUD(9, "澳大利亚元"),
    SGD(10, "新加坡元"),
    AUD2(11, "澳元"),
    ;

    private Integer code;

    private String desc;

    AmountUnitEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AmountUnitEnum getByCode(String code) {
        for (AmountUnitEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
