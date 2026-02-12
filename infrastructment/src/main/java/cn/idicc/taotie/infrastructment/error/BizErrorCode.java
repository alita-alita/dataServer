package cn.idicc.taotie.infrastructment.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: WangZi
 * @Date: 2022/12/14
 * @Description: 业务异常枚举
 * @version: 1.0
 */
@Getter
@AllArgsConstructor
public enum BizErrorCode implements IErrorCode {
    /**
     * 短信验证码发送失败
     */
    SMS_CODE_SEND_FAIL("SMS_CODE_SEND_FAIL", "短信验证码发送失败"),
    /**
     * 短信验证码当日获取次数已达上限
     */
    SMS_CODE_DAY_LIMIT("SMS_CODE_DAY_LIMIT", "短信验证码当日获取次数已达上限"),
    /**
     * 短信验证码已使用
     */
    SMS_CODE_USED("SMS_CODE_USED", "短信验证码已使用"),
    /**
     * 短信验证码已过期
     */
    SMS_CODE_EXPIRED("SMS_CODE_EXPIRED", "短信验证码已过期"),
    /**
     * 短信验证码错误
     */
    SMS_CODE_ERROR("SMS_CODE_ERROR", "短信验证码错误"),
    /**
     * excel模板格式错误
     */
    EXCEL_MODULE_ERROR("EXCEL_MODULE_ERROR", "excel模板格式错误"),

    ENTERPRISE_NAME_NO_EXIT("ENTERPRISE_NAME_NO_EXIT", "企业名称不存在"),

    LABEL_NAME_NO_EXIT("LABEL_NAME_NO_EXIT", "标签名称不存在"),

    LABEL_NAME_VOID("LABEL_NAME_VOID", "标签名称无效"),

    UNIFIED_SOCIAL_CREDIT_NO_EXIT("UNIFIED_SOCIAL_CREDIT_NO_EXIT", "社会统一信用代码不存在"),

    UNIFIED_SOCIAL_CREDIT_REPEAT("UNIFIED_SOCIAL_CREDIT_REPEAT", "社会统一信用代码重复"),

    UNIFIED_SOCIAL_CREDIT_VOID("UNIFIED_SOCIAL_CREDIT_VOID", "社会统一信用代码无效"),

    STOCK_CODE_NO_EXIT("STOCK_CODE_NO_EXIT", "股票代码不存在"),

    INDUSTRY_CHAIN_NODE_ENTERPRISE_NOT_EXIST("INDUSTRY_CHAIN_NODE_ENTERPRISE_NOT_EXIST", "产业链节点企业信息不存在"),

    ORG_NOT_EXIST("ORG_NOT_EXIST", "机构信息不存在"),

    ADMINISTRATIVE_DIVISION_NOT_EXIST("ADMINISTRATIVE_DIVISION_NOT_EXIST", "区域code无效"),

    INVESTMENT_ENTERPRISE_RECOMMEND_ADD_ERROR("INVESTMENT_ENTERPRISE_RECOMMEND_ADD_ERROR", "招商推荐信息新增失败"),

    INVESTMENT_ENTERPRISE_RECOMMEND_BATCH_COPY_ERROR("INVESTMENT_ENTERPRISE_RECOMMEND_BATCH_COPY_ERROR", "招商推荐信息批量复制失败"),

    INDUSTRY_CHAIN_NODE_NOT_EXIST("INDUSTRY_CHAIN_NODE_NOT_EXIST", "产业链节点信息不存在"),
    ;

    private final String code;

    private final String message;
}
