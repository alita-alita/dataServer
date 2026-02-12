package cn.idicc.taotie.infrastructment.po.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/5/17
 * @Description: 招商委托任务es索引
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "investment_entrust_task", createIndex = false)
public class InvestmentEntrustTaskPO extends BaseSearchPO {

    /**
     * 招商线索id
     */
    private Long clueId;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 委托机构id
     */
    private Long orgId;

    /**
     * 委托机构名称
     */
    private String orgName;

    /**
     * 委托机构code
     */
    private String orgCode;

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
    private Long entrustTimeStamp;

    /**
     * 任务到期时间戳
     */
    private Long maturityTimeStamp;

    /**
     * 任务完成时剩余时间戳
     */
    private Long finishRemainTimeStamp;

    /**
     * 跟进状态 0未开始 1进行中 2已完成 3延期
     */
    private Integer followUpStatus;

    /**
     * 委托备注
     */
    private String remark;

    /**
     * 产业链id集合
     */
    private List<Long> chainIds;

    /**
     * 企业标签id集合
     */
    private List<Long> enterpriseLabelIds;
}
