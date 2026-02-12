package cn.idicc.taotie.infrastructment.error;

import lombok.Data;


/**
 * @Author: WangZi
 * @Date: 2022/12/14
 * @Description: ErrorDTO
 * @version: 1.0
 */
@Data
public class ErrorDTO {

    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMsg;

    public ErrorDTO(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ErrorDTO(IErrorCode enumError) {
        this(enumError.getCode(), enumError.getMessage());
    }
}
