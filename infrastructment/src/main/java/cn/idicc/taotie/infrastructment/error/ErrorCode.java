package cn.idicc.taotie.infrastructment.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局错误码
 * @author yannlu
 */
@Getter
@AllArgsConstructor
public enum ErrorCode implements IErrorCode {
    /**
     * 请求成功
     */
    SUCCESS("SUCCESS", "请求成功"),
    /**
     * 系统繁忙
     */
    SYSTEM_BUSY("SYSTEM_BUSY", "系统繁忙"),
    /**
     * 系统异常，请稍后重试
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常，请稍后重试"),
    /**
     * 参数错误
     */
    PARAMS_ERROR("PARAMS_ERROR", "参数错误"),
    /**
     * 缺少参数
     */
    MISSING_PARAMS("MISSING_PARAMS", "缺少参数"),
    /**
     * 请求重定向
     */
    REDIRECT("REDIRECT", "请求重定向"),
    /**
     * 无效请求
     */
    INVALID_REQUEST("INVALID_REQUEST", "无效的请求"),
    /**
     * 未认证请求
     */
    UNAUTHORIZED("UNAUTHORIZED", "未认证的请求"),
    /**
     * 禁止请求
     */
    FORBIDDEN("FORBIDDEN", "禁止的请求"),
    /**
     * 未允许请求
     */
    NOT_ALLOWED("NOT_ALLOWED", "无权限访问"),
    /**
     * 请求的资源不存在
     */
    NOT_FOUND("NOT_FOUND", "请求的资源不存在"),
    /**
     * 请求后端无效
     */
    BAD_GATEWAY("BAD_GATEWAY", "请求后端无效"),
    /**
     * 请求超时
     */
    TIMEOUT("TIMEOUT", "请求超时"),
    /**
     * 无效的Token
     */
    INVALID_TOKEN("INVALID_TOKEN", "无效的Token"),
    /**
     * 流量控制
     */
    FLOW_CONTROL("FLOW_CONTROL", "流量控制"),
    /**
     * 缓存失效
     */
    CACHE_NOT_EXIST("CACHE_NOT_EXIST", "缓存已失效"),
    /**
     * 数据不存在
     */
    NOT_EXIST("NOT_EXIST", "数据不存在"),
    /**
     * 数据不能为空
     */
    NOT_EMPTY("NOT_EMPTY", "数据不能为空"),
    /**
     * 保存失败
     */
    SAVE_FAIL("SAVE_FAIL", "保存失败"),
    /**
     * 删除失败
     */
    DELETE_FAIL("DELETE_FAIL", "删除失败"),
    /**
     * 上传文件失败
     */
    UPLOAD_FILE_ERROR("UPLOAD_FILE_ERROR", "上传文件失败"),
    /**
     * 未登录或已超时
     */
    LOGIN_EXPIRE("LOGIN_EXPIRE", "未登录或已超时"),
    /**
     * 登录失败
     */
    LOGIN_FAIL("LOGIN_FAIL", "你输入的账号或密码不正确，请重新输入"),
    /**
     * 账号已禁用
     */
    ACCOUNT_DISABLE("ACCOUNT_DISABLE", "账号被禁用或已过期，请联系管理员"),
    /**
     * rsa解密失败
     */
    RSA_DECRYPT_ERROR("RSA_DECRYPT_ERROR", "rsa解密失败"),
    /**
     * 数据重复
     */
    DUPLICATE_DATA("DUPLICATE_DATA", "数据重复"),
    /**
     * 唯一索引冲突
     */
    UNIQUE_INDEX_COLLISION("UNIQUE_INDEX_COLLISION", "唯一索引冲突"),
    /**
     * CompletableFuture多线程处理异常
     */
    COMPLETABLE_FUTURE_ERROR("COMPLETABLE_FUTURE_ERROR", "CompletableFuture多线程处理异常"),


    PARAMETER_EMPTY("PARAMETER_EMPTY","请求参数不可为空"),
    ;

    private final String code;

    private final String message;
}
