package cn.idicc.taotie.infrastructment.enums;

/**
 * @Author: wd
 * @Date: 2023/5/22
 * @Description:产业链节点更显类型枚举
 * @version: 1.0
 */
public enum IndustryChainChangeTypeEnum {

    ADD_NODE(1, "新增节点"),
    DELETE_NODE(2, "删除节点"),
    UPDATE_NODE(3, "修改产业链"),
    ;

    private Integer code;

    private String desc;

    IndustryChainChangeTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static IndustryChainChangeTypeEnum getByCode(Integer code) {
        for (IndustryChainChangeTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
