package cn.idicc.taotie.infrastructment.enums;

import java.util.Arrays;
import java.util.List;

public enum RecordVersionStateEnum {

    DRAFT(0,"草稿"),
    PRODUCE(1,"生产中"),

    MOUNTING(2,"挂载中"),
    MOUNTING_STOP(3,"生产(挂载)停止"),

    PENDING_QUALITY_INSPECTING(4,"待质检"),
    DW_ONLINE_NOW(5,"集市发布中"),
    DW_ONLINE_COMPLETE(6,"集市发布完成"),
    ONLINE_NOW(7,"上线中"),
    ONLINE_COMPLETE(8,"上线完成"),

    KNOWLEDGE_GENERATE(9,"知识库生成中"),


    ERROR(100,"失败"),
    ;

    private Integer code;

    private String desc;

    RecordVersionStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static List<RecordVersionStateEnum> getChainAllowProducing(){
        return Arrays.asList(
                RecordVersionStateEnum.DRAFT,
                RecordVersionStateEnum.MOUNTING_STOP,
                RecordVersionStateEnum.PENDING_QUALITY_INSPECTING
        );
    }

    public static List<RecordVersionStateEnum> getChainAllowStop(){
        return Arrays.asList(
                RecordVersionStateEnum.PRODUCE,
                RecordVersionStateEnum.MOUNTING
        );
    }

    public static List<RecordVersionStateEnum> getChainAllowEdit(){
        return Arrays.asList(
                RecordVersionStateEnum.DRAFT,
                RecordVersionStateEnum.MOUNTING_STOP
        );
    }

    public static RecordVersionStateEnum getByCode(Integer code) {
        for (RecordVersionStateEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}
