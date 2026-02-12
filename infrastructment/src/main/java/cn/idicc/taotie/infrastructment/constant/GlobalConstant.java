package cn.idicc.taotie.infrastructment.constant;

/**
 * @Author: WangZi
 * @Date: 2022/12/14
 * @Description: 全局常量
 * @version: 1.0
 */
public class GlobalConstant {

    /**
     * 登录token redis key前缀
     */
    public static final String TOKEN_KEY = "TOKEN_KEY:";

    /**
     * 登出token黑名单redis key前缀
     */
    public static final String TOKEN_BLACKLIST = "TOKEN_BLACKLIST:";

    /**
     * 重复发送验证码redis key前缀
     */
    public static final String REPEAT_SEND_VERIFY_CODE = "REPEAT_SEND_VERIFY_CODE:";

    /**
     * 短信验证码过期时间
     */
    public static final int SMS_CODE_EXPIRED = 600000;

    /**
     * 短信验证码每日获取上限条数
     */
    public static final int SMS_CODE_DAY_LIMIT = 3;

    /**
     * 斜杠
     */
    public static final String SLASH = "/";

    /**
     * 句号
     */
    public static final String PERIOD = ".";

    /**
     * 双横杠
     */
    public static final String DOUBLE_WHIPPLETREE = "--";

    /**
     * 横杠
     */
    public static final String WHIPPLETREE = "-";

    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 百分号
     */
    public static final String PERCENT = "%";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 中文逗号
     */
    public static final String COMMA_CHINESE = "，";

    /**
     * 分号
     */
    public static final String SEMICOLON = ";";

    /**
     * 中文分号
     */
    public static final String SEMICOLON_CHINESE = "；";

    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 分割号
     */
    public static final String SEPARATRIX = "|";

    /**
     * 点
     */
    public static final String POINT = ".";

    /**
     * 顿号
     */
    public static final String PAUSE = "、";

    /**
     * *号
     */
    public static final String ASTERISK = "*";

    public static final Integer PAGE_SIZE = 500;

    public static final Integer ZERO = 0;

    public static final Integer ONE = 1;

    public static final Integer TWO = 2;

    public static final Integer THREE = 3;

    public static final Integer FOUR = 4;

    public static final Integer FIVE = 5;

    public static final Integer EIGHT = 8;

    public static final Integer TEN = 10;

    public static final Integer THIRTEEN = 13;

    public static final Integer FOURTEEN = 14;

    public static final Integer TWENTY = 20;

    public static final Integer THIRTY = 30;

    public static final Integer FIFTY = 50;

    public static final Integer ONE_HUNDRED = 100;

    public static final Integer REDIS_LOCK_TIME = 10;


    /**
     * 更新时间参数名
     */
    public static final String GMT_MODIFY_PARAM_KEY = "gmtModify";

    /**
     * 逻辑删除参数名
     */
    public static final String DELETED_PARAM_KEY = "deleted";

    /**
     * 默认系统用户
     */
    public static final String DEFAULT_USER = "system";

    /**
     * mdc中请求id的key
     */
    public static final String REQUEST_ID = "requestId";

    /**
     * 总条数
     */
    public static final String TOTAL_RECORD = "total";

    /**
     * 已经消费记录
     */
    public static final String CONSUMED_RECORD = "consumed";
    public static final String BATCH_COUNT_KEY = "batch_count";


    public static final String UNIT = "元";

    /**
     * 单位万
     */
    public static final String UNIT_THOUSAND = "万";

    /**
     * excel文件的.xlsx后缀
     */
    public static final String EXCEL_XLSX_SUFFIX = ".xlsx";
}
