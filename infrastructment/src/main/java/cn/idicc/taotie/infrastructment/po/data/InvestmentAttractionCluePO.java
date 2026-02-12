package cn.idicc.taotie.infrastructment.po.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * @Author: wd
 * @Date: 2023/5/11
 * @Description: 招商线索PO
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "investment_attraction_clue", createIndex = false)
public class InvestmentAttractionCluePO extends BaseSearchPO {

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 社会统一信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 推荐企业所在省
     */
    private String province;

    /**
     * 推荐企业所在市
     */
    private String city;

    /**
     * 推荐企业所在区
     */
    private String area;

    /**
     * 成立日期
     */
    private Long registerDate;

    /**
     * 入驻机构id
     */
    private Long orgId;

    /**
     * 机构code
     */
    private String orgCode;

    /**
     * 入驻机构名称
     */
    private String orgName;

    /**
     * 是否结束 0否 1是
     */
    private Boolean isEnd;

    /**
     * 线索指派状态 0待指派 1已指派
     */
    private Integer clueState;

    /**
     * 是否系统推荐 0否1是
     */
    private Boolean isSystemRecommend;

    /**
     * 线索被指派人id
     */
    private Long beAssignPersonId;

    /**
     * 线索被指派人
     */
    private String beAssignPerson;

    /**
     * 是否委托 0否 1是
     */
    private Boolean entrustOrNot;

    /**
     * 是否有投资意向 0否 1是
     */
    private Boolean havaInvestmentIntention;

    /**
     * 意向日期
     */
    private Long intentionDate;

    /**
     * 推荐日期
     */
    private Long recommendDate;

    /**
     * 推荐理由
     */
    private List<String> recommendationReason;

    /**
     * 推荐理由详情
     */
    private String recommendationReasonDetail;

    /**
     * 纳入意向人id
     */
    private Long intentionPersonId;

    /**
     * 纳入意向人
     */
    private String intentionPerson;

    /**
     * 来源
     */
    private Integer clueSource;

    /**
     * 指派人id
     */
    private Long assignPersonId;

    /**
     * 指派人
     */
    private String assignPerson;

    /**
     * 指派日期
     */
    private Long assignDate;

    /**
     * 线索处理状态 0待处理 1处理中 2已完成
     */
    private Integer clueDealState;

    /**
     * 指派备注
     */
    private String assignRemark;

    /**
     * 走访日期
     */
    private Long visitDate;

    /**
     * 当前走访人id
     */
    private Long visitPersonId;

    /**
     * 当前走访人
     */
    private String visitPerson;

    /**
     * 是否添加走访记录
     */
    private Boolean isAddVisit;

    /**
     * 委托人id
     */
    private Long entrustPersonId;

    /**
     * 委托人
     */
    private String entrustPerson;

    /**
     * 委托时间戳
     */
    private Long entrustDate;

    /**
     * 委托处理状态
     */
    private Integer entrustDealState;

    /**
     * 历史走访人
     */
    private List<Long> hisVisitPersonIds;

    /**
     * 历史被指派人
     */
    private List<Long> hisBeAssignPersonIds;

    /**
     * 历史指派人
     */
    private List<Long> hisAssignPersonIds;

    /**
     * 产业链id集合
     */
    private List<Long> chainIds;

    /**
     * 企业标签id集合
     */
    private List<Long> enterpriseLabelIds;

    /**
     * 产业链标签id集合
     */
    private List<Long> industryLabelIds;

    /**
     * 企业关联产业链二级节点名称集合
     */
    private List<String> secondChainNodeNames;

    /**
     * 企业关联二级节点id集合
     */
    private List<Long> secondChainNodeIds;
}
