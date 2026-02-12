package cn.idicc.taotie.infrastructment.enums;

import lombok.Getter;

/**
 * @Author: WangZi
 * @Date: 2023/3/28
 * @Description: 上传文件状态枚举
 * @version: 1.0
 */
@Getter
public enum UploadFileStatusEnum {

    TO_BE_PROCESSED(0, "待处理"),

    NO_DEAL(1, "不必处理"),

    DEAL_SUCCESS(2, "处理成功"),

    DEAL_FAIL(3, "处理失败"),
    ;

    private Integer code;

    private String name;

    UploadFileStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UploadFileStatusEnum getByCode(Integer code) {
        for (UploadFileStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
