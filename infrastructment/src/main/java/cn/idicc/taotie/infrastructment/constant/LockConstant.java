package cn.idicc.taotie.infrastructment.constant;

/**
 * @Author: WangZi
 * @Date: 2023/1/30
 * @Description: 全局锁常量
 * @version: 1.0
 */
public class LockConstant {

    /**
     * 导入产业链标签分布式锁前缀
     */
    public static final String UPLOAD_INDUSTRY_LABEL = ":UPLOAD_INDUSTRY_LABEL";

    /**
     * 导入企业标签分布式锁前缀
     */
    public static final String UPLOAD_ENTERPRISE_LABEL = ":UPLOAD_ENTERPRISE_LABEL";

    /**
     * 导入企业信息分布式锁前缀
     */
    public static final String UPLOAD_ENTERPRISE = ":UPLOAD_ENTERPRISE";

    /**
     * 导入企业信息和产业链标签的绑定关系分布式锁前缀
     */
    public static final String UPLOAD_ENTERPRISE_INDUSTRY_LABEL_RELATION = ":UPLOAD_ENTERPRISE_INDUSTRY_LABEL_RELATION";

    /**
     * 导入企业信息和企业标签的绑定关系分布式锁前缀
     */
    public static final String UPLOAD_ENTERPRISE_CORRELATION_LABEL = ":UPLOAD_ENTERPRISE_CORRELATION_LABEL";

    /**
     * 导入上市企业信息分布式锁前缀
     */
    public static final String UPLOAD_LISTED_COMPANY = ":UPLOAD_LISTED_COMPANY";

    /**
     * 导入招商资讯分布式锁前缀
     */
    public static final String UPLOAD_INVESTMENT_INFORMATION = ":UPLOAD_INVESTMENT_INFORMATION";

    /**
     * 地图查询分布式锁前缀
     */
    public static final String MAP_QUERY = ":MAP_QUERY";

    /**
     * 图谱查询分布式锁前缀
     */
    public static final String ATLAS_QUERY = ":ATLAS_QUERY";

    /**
     * 按产业链节点查询图谱数据分布式锁前缀
     */
    public static final String LEAF_NODE_ENTERPRISE_NUM_QUERY = ":LEAF_NODE_ENTERPRISE_NUM_QUERY";

    /**
     * InvestmentManagementJob的checkClueState方法的任务锁
     */
    public static final String INVESTMENT_MANAGEMENT_JOB_CHECK_CLUE_STATE_JOB_KEY = ":INVESTMENT_MANAGEMENT_JOB_CHECK_CLUE_STATE_JOB_KEY";

    /**
     * InvestmentManagementJob的dealDefunctBusinessInvitationRelation方法的任务锁
     */
    public static final String INVESTMENT_MANAGEMENT_JOB_DEAL_DEFUNCT_BUSINESS_INVITATION_RELATION_JOB_KEY = ":INVESTMENT_MANAGEMENT_JOB_DEAL_DEFUNCT_BUSINESS_INVITATION_RELATION_JOB_KEY";

    /**
     * InvestmentEntrustTaskJob的expiredTask方法的任务锁
     */
    public static final String INVESTMENT_ENTRUST_TASK_JOB_EXPIRED_TASK_JOB_KEY = ":INVESTMENT_ENTRUST_TASK_JOB_EXPIRED_TASK_JOB_KEY";

    /**
     * 初始化企业数据到es锁
     */
    public static final String INIT_ENTERPRISE_DATA_TO_ES = ":INIT_ENTERPRISE_DATA_TO_ES";

    /**
     * 初始化招商线索数据到es锁
     */
    public static final String INIT_INVESTMENT_ATTRACTION_CLUE_DATA_TO_ES = ":INIT_INVESTMENT_ATTRACTION_CLUE_DATA_TO_ES";

    /**
     * 初始化招商企业数据到es锁
     */
    public static final String INIT_INVESTMENT_ENTERPRISE_DATA_TO_ES = ":INIT_INVESTMENT_ENTERPRISE_DATA_TO_ES";

    /**
     * 初始化招商企业推荐记录数据到es锁
     */
    public static final String INIT_INVESTMENT_ENTERPRISE_RECOMMEND_DATA_TO_ES = ":INIT_INVESTMENT_ENTERPRISE_RECOMMEND_DATA_TO_ES";

    /**
     * 初始化招商委托任务数据到es锁
     */
    public static final String INIT_INVESTMENT_ENTRUST_TASK_DATA_TO_ES = ":INIT_INVESTMENT_ENTRUST_TASK_DATA_TO_ES";

    /**
     * 初始化企业数标签据到es锁
     */
    public static final String INIT_ENTERPRISE_LABEL_DATA_TO_ES = ":INIT_ENTERPRISE_LABEL_DATA_TO_ES";

    /**
     * 初始化产业链数据到es锁
     */
    public static final String INIT_INDUSTRY_CHAIN_DATA_TO_ES = ":INIT_INDUSTRY_CHAIN_DATA_TO_ES";

    /**
     * 初始化产业链数据到es锁
     */
    public static final String SYNC_INDUSTRY_CHAIN_DATA_TO_ES = ":SYNC_INDUSTRY_CHAIN_DATA_TO_ES";

    /**
     * 初始化产业链标签数据到es锁
     */
    public static final String INIT_INDUSTRY_LABEL_DATA_TO_ES = ":INIT_INDUSTRY_LABEL_DATA_TO_ES";

    /**
     * 初始化入驻机构数据到es锁
     */
    public static final String INIT_INSTITUTION_DATA_TO_ES = ":INIT_INSTITUTION_DATA_TO_ES";

    /**
     * 同步企业数据到es锁
     */
    public static final String SYNC_ENTERPRISE_DATA_TO_ES = ":SYNC_ENTERPRISE_DATA_TO_ES";

    /**
     * 同步招商企业推荐信息数据到es锁
     */
    public static final String SYNC_INVESTMENT_ENTERPRISE_RECOMMEND_DATA_TO_ES = ":SYNC_INVESTMENT_ENTERPRISE_RECOMMEND_DATA_TO_ES";

    /**
     * 同步招商企业数据到es锁
     */
    public static final String SYNC_INVESTMENT_ENTERPRISE_DATA_TO_ES = ":SYNC_INVESTMENT_ENTERPRISE_DATA_TO_ES";

    /**
     * 同步招商线索数据到es锁
     */
    public static final String SYNC_INVESTMENT_ATTRACTION_CLUE_DATA_TO_ES = ":SYNC_INVESTMENT_ATTRACTION_CLUE_DATA_TO_ES";

    /**
     * 同步招商委托任务数据到es锁
     */
    public static final String SYNC_INVESTMENT_ENTRUST_TASK_DATA_TO_ES = ":SYNC_INVESTMENT_ENTRUST_TASK_DATA_TO_ES";

    /**
     * 新增招商推荐记录分布式锁
     */
    public static final String ADD_INVESTMENT_ENTERPRISE_RECOMMEND = ":ADD_INVESTMENT_ENTERPRISE_RECOMMEND";
}
