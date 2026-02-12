package cn.idicc.taotie.infrastructment.enums;

public enum RecordVersionBusinessUkEnum {

    CHAIN(1, "CHAIN"),
    ;

    private Integer code;

    private String desc;

    RecordVersionBusinessUkEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RecordVersionBusinessUkEnum getByCode(Integer code) {
        for (RecordVersionBusinessUkEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}
