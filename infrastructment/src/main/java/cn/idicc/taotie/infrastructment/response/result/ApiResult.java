package cn.idicc.taotie.infrastructment.response.result;


import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.error.ErrorDTO;
import cn.idicc.taotie.infrastructment.error.IErrorCode;
import cn.idicc.taotie.infrastructment.exception.BizException;
import lombok.Data;
import org.slf4j.MDC;

/**
 * @Author: WangZi
 * @Date: 2022/12/14
 * @Description: API控制层统一返回包装类
 * @version: 1.0
 */
@Data
public class ApiResult<T> {

    /**
     * 请求返回结果，如果正常则返回数据，异常则返回统一异常
     * {@link ErrorCode}
     */
    private T result;

    private String code;

    private String msg;

    /**
     * log MDC 请求id
     */
    private String requestId;

    public ApiResult(String code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
        try {
            this.requestId = MDC.get("requestId");
        } catch (Exception e) {

        }
    }

    public static ApiResult<ErrorDTO> error(IErrorCode error) {
        return new ApiResult<>(error.getCode(), error.getMessage(), null);
    }

    public static ApiResult error(String code, String msg) {
        return new ApiResult(code, msg, null);
    }

    public static ApiResult error(IErrorCode error, Object result) {
        return new ApiResult(error.getCode(), error.getMessage(), result);
    }

    public static ApiResult error(BizException e) {
        return new ApiResult(e.getCode(), e.getMessage(), e.getData());
    }

    public static ApiResult success(Object result) {
        return new ApiResult(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), result);
    }

    public static ApiResult success() {
        return new ApiResult(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }
}
