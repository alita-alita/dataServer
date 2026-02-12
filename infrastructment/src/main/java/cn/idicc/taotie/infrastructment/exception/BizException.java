package cn.idicc.taotie.infrastructment.exception;

import cn.idicc.taotie.infrastructment.constant.GlobalConstant;
import cn.idicc.taotie.infrastructment.error.IErrorCode;
import lombok.Getter;

/**
 * @Author: WangZi
 * @Date: 2022/12/14
 * @Description: 通用业务异常类
 * @version: 1.0
 */
@Getter
public class BizException extends RuntimeException {

    private static final String ERROR_CODE = "500";

    private String code;

    private String message;

    private Object data;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
        this.message = message;
        this.code = ERROR_CODE;
    }

    public BizException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
        this.code = ERROR_CODE;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.message = message + GlobalConstant.COLON + cause.getMessage();
        this.code = ERROR_CODE;
    }

    public BizException(String code, String message) {
        this(code, message, null, null);
    }

    public BizException(IErrorCode enumError) {
        this(enumError.getCode(), enumError.getMessage(), null, null);
    }

    public BizException(IErrorCode enumError, Object data) {
        this(enumError.getCode(), enumError.getMessage(), null, data);
    }

    public BizException(IErrorCode enumError, Throwable cause) {
        this(enumError.getCode(), enumError.getMessage(), cause, null);
    }

    public BizException(String code, String message, Throwable cause, Object data) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
