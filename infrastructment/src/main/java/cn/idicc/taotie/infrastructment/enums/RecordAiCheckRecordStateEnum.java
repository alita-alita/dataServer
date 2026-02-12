package cn.idicc.taotie.infrastructment.enums;

public enum RecordAiCheckRecordStateEnum {

    DEFAULT(0,"待执行"),

    RUNNING(1,"执行中"),

    PASS(2,"质检通过"),
    NOT_PASS(3,"质检不通过,待人工确认"),
    NOT_PASS_READY(4,"质检不通过,已人工确认"),

    ERROR(100,"失败"),
    ;

    private Integer code;

    private String desc;

    RecordAiCheckRecordStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RecordAiCheckRecordStateEnum getByCode(Integer code) {
        for (RecordAiCheckRecordStateEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}
