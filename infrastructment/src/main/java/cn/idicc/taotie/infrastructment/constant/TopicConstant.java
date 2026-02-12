package cn.idicc.taotie.infrastructment.constant;

/**
 * topic常量
 *
 * @author wd
 * @date 2022-12-20
 */
public class TopicConstant {

    // 初始化产业链节点
    public static final String INIT_CHAIN_NODE = "INIT_CHAIN_NODE";
    //新增产业链节点
    public static final String ADD_CHAIN_NODE = "ADD_CHAIN_NODE";
    // 节点标签变更
    public static final String UPDATE_CHAIN_NODE_LABEL = "UPDATE_CHAIN_NODE_LABEL";
    // 更新节点企业评分
    public static final String UPDATE_CHAIN_NODE_ENTERPRISE_SCORE = "UPDATE_CHAIN_NODE_ENTERPRISE_SCORE";

    /**
     * 导入招商资讯通知主题
     */
    public static final String UPLOAD_INVESTMENT_INFORMATION_TOPIC = "UPLOAD_INVESTMENT_INFORMATION_TOPIC";

    /**
     * 导入招商推荐通知主题
     */
    public static final String UPLOAD_INVESTMENT_RECOMMENDATION_TOPIC = "UPLOAD_INVESTMENT_RECOMMENDATION_TOPIC";

    /**
     * 企业数据变更需要更新es数据的通知主题
     */
    public static final String SEND_ENTERPRISE_CHANGE_ADVICE = "SEND_ENTERPRISE_CHANGE_ADVICE";

    /**
     * 企业es索引数据变更需要通知其他关联企业es数据的es索引做同步变更的通知主题
     */
    public static final String SEND_RELATION_ENTERPRISE_CHANGE_ADVICE = "SEND_RELATION_ENTERPRISE_CHANGE_ADVICE";

    /**
     * 招商线索发生变更，需要同步招商线索es数据的通知主题
     */
    public static final String SEND_INVESTMENT_ATTRACTION_CLUE_CHANGE_ADVICE = "SEND_INVESTMENT_ATTRACTION_CLUE_CHANGE_ADVICE";

    /**
     * 招商企业发生变更，需要同步招商企业es数据的通知主题
     */
    public static final String SEND_INVESTMENT_ENTERPRISE_CHANGE_ADVICE = "SEND_INVESTMENT_ENTERPRISE_CHANGE_ADVICE";

    /**
     * 推荐招商企业信息发生变更，需要同步推荐招商企业信息es数据的通知主题
     */
    public static final String SEND_INVESTMENT_ENTERPRISE_RECOMMEND_CHANGE_ADVICE = "SEND_INVESTMENT_ENTERPRISE_RECOMMEND_CHANGE_ADVICE";

    /**
     * 产业链节点变更需要更新es数据的通知主题
     */
    public static final String SEND_INDUSTRY_NODE_CHANGE_ADVICE = "SEND_INDUSTRY_NODE_CHANGE_ADVICE";

    public static final String PRODUCT_AI_CHECK_1 = "taotie_product_ai_check_1";
}
