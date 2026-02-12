package cn.idicc.taotie.service.handler;

import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.concurrent.CompletionException;

/**
 * @Author: WangZi
 * @Date: 2022/12/14
 * @Description: 全局异常统一处理器
 * @version: 1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理CompletableFuture多线程执行出现的异常
     *
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(value = CompletionException.class)
    @ResponseBody
    public ApiResult<?> completionExceptionHandler(HttpServletRequest req, Exception ex) {
        log.error("[completionExceptionHandler]", ex);
        return ApiResult.error(ErrorCode.COMPLETABLE_FUTURE_ERROR.getCode(), ex.getMessage());
    }

    /**
     * 处理唯一索引插入冲突异常
     *
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody
    public ApiResult<?> duplicateKeyExceptionHandler(HttpServletRequest req, Exception ex) {
        log.error("[duplicateKeyExceptionHandler]", ex);
        return ApiResult.error(ErrorCode.UNIQUE_INDEX_COLLISION.getCode(), String.format(ErrorCode.UNIQUE_INDEX_COLLISION.getMessage() + ":%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ApiResult<?> missingServletRequestParameterExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return ApiResult.error(ErrorCode.MISSING_PARAMS.getCode(), String.format("请求参数缺失:%s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ApiResult<?> methodArgumentTypeMismatchExceptionHandler(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(), String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResult<?> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
//        log.warn("[methodArgumentNotValidExceptionExceptionHandler]", ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        System.out.println(fieldError);
        Assert.notNull(fieldError, "fieldError为空");
        String errorMsg = String.format("请求参数为空: %s", fieldError.getDefaultMessage());
        return ApiResult.error(ErrorCode.PARAMETER_EMPTY.getCode(), errorMsg);
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ApiResult<?> bindExceptionHandler(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        Assert.notNull(fieldError, "fieldError为空");
        String errorMsg = String.format("请求参数不正确:%s", fieldError.getDefaultMessage());
        return ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(), errorMsg);
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ApiResult<?> constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(), String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     * <p>
     * 注意，它需要设置如下两个配置项：
     * 1. spring.mvc.throw-exception-if-no-handler-found 为 true
     * 2. spring.mvc.static-path-pattern 为 /statics/**
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ApiResult<?> noHandlerFoundExceptionHandler(HttpServletRequest req, NoHandlerFoundException ex) {
        log.warn("[noHandlerFoundExceptionHandler]", ex);
        return ApiResult.error(ErrorCode.INVALID_REQUEST.getCode(), String.format("请求地址不存在:%s", ex.getRequestURL()));
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     * <p>
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ApiResult<?> httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {
        log.warn("[httpRequestMethodNotSupportedExceptionHandler]", ex);
        return ApiResult.error(ErrorCode.NOT_FOUND.getCode(), String.format("请求方法不正确:%s", ex.getMessage()));
    }

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ApiResult<?> illegalArgumentExceptionHandler(HttpServletRequest req, IllegalArgumentException e) {
        log.error("IllegalArgumentException:", e);
        return ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ApiResult<?> bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("发生业务异常！", e);
        return ApiResult.error(e);
    }


    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseBody
    public ApiResult<?> illegalStateExceptionHandler(HttpServletRequest req, IllegalStateException e) {
        log.error("illegalStateException:", e);
        return ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(), String.format("必填项不能为空:%s", e.getMessage()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ApiResult<?> httpMessageNotReadableExceptionHandler(HttpServletRequest req, HttpMessageNotReadableException e) {
        log.error("httpMessageNotReadableException:", e);
        return ApiResult.error(ErrorCode.PARAMS_ERROR.getCode(), "请求对象不能为空");
    }

    /**
     * 处理非业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult<?> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("发生异常！", e);
        return ApiResult.error(ErrorCode.SYSTEM_BUSY);
    }
}
