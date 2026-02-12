package cn.idicc.taotie.infrastructment.enums;

public enum DataWatchTypeEnum {

    //"0产业链企业,1领军人才,2创业企业,3企业产品,4乡贤企业,5校友企业"
    /**
     * 产业链企业
     */
    CHAIN_ENTERPRISE( 0, "产业链企业", 0),

    /**
     * 产业链领军人才
     */
    LEADING_TALENT( 1, "产业链领军人才", 0),

    /**
     * 产业链创业项目
     */
    STARTUP_PROJECT( 2, "产业链创业项目", 0),

    /**
     * 产业链产品
     */
    ENTERPRISE_PRODUCT(3, "产业链产品", 0),

    /**
     * 地区乡贤企业
     */
    ANCESTOR_ENTERPRISE(4, "地区乡贤企业", 1),

    /**
     * 地区校友企业
     */
    ACADEMIA_ENTERPRISE(5, "地区校友企业", 1),

    ;

    private Integer code;

    private String name;

    /**
     * 关联数据类型（0产业链数据，1地区数据）
     */
    private Integer relateDataType;

    DataWatchTypeEnum(Integer code, String name, Integer relateDataType) {
        this.code = code;
        this.name = name;
        this.relateDataType = relateDataType;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getRelateDataType() {
        return relateDataType;
    }

    public void setRelateDataType(Integer relateDataType) {
        this.relateDataType = relateDataType;
    }

    public static DataWatchTypeEnum getByCode(Integer code) {
        for (DataWatchTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
