package cn.idicc.taotie.infrastructment.enums;

/**
 * @Author: wd
 * @Date: 2023/5/4
 * @Description:我的业务数据类型枚举
 * @version: 1.0
 */
public enum MyCollectDataTypeEnum {

    COLLECT_INFORMATION(1, "收藏资讯"),
    FOCUS_ENTERPRISE(2, "关注企业"),
    ;

    private Integer code;

    private String desc;

    MyCollectDataTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static MyCollectDataTypeEnum getByCode(String code) {
        for (MyCollectDataTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
