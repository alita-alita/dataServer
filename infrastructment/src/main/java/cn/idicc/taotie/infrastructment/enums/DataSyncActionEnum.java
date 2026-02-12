package cn.idicc.taotie.infrastructment.enums;

/**
 * @Author: MengDa
 * @Date: 2023/8/15
 * @Description:
 * @version: 1.0
 */
public enum DataSyncActionEnum {
    INSERT(1, "INSERT"),
    UPDATE(2, "UPDATE"),
    DELETE(3, "DELETE"),

            ;

    private Integer code;

    private String desc;

    DataSyncActionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DataSyncActionEnum getByCode(Integer code) {
        for (DataSyncActionEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static DataSyncActionEnum getByDesc(String desc) {
        for (DataSyncActionEnum item : values()) {
            if (item.getDesc().equals(desc)) {
                return item;
            }
        }
        return null;
    }
}
