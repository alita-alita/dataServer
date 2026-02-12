package cn.idicc.taotie.infrastructment.enums;

import java.util.Arrays;
import java.util.List;

public enum RecordAiCheckStateEnum {

    DEFAULT(0,"待执行"),

    STARTING(1,"启动中"),

    RUNNING(2,"运行中"),

    STOP(3,"已停止"),

    WAITING_MANUAL(4,"待人工确认"),

    EXPORTING(5,"导入下游中"),
    
    COMPLETE(6,"已完成"),

    ERROR(100,"失败"),
    ;

    private Integer code;

    private String desc;

    RecordAiCheckStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static List<RecordAiCheckStateEnum> getAllowRunning(){
        return Arrays.asList(
                RecordAiCheckStateEnum.DEFAULT,
                RecordAiCheckStateEnum.STOP,
                RecordAiCheckStateEnum.ERROR
        );
    }

    public static List<RecordAiCheckStateEnum> getAllowStop(){
        return Arrays.asList(
                RecordAiCheckStateEnum.RUNNING
        );
    }

    public static RecordAiCheckStateEnum getByCode(Integer code) {
        for (RecordAiCheckStateEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}
