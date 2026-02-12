package cn.idicc.taotie.infrastructment.po.data;

import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseRecommendDO;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Author: WangZi
 * @Date: 2023/5/8
 * @Description: 招商企业推荐记录es索引实体类
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "investment_enterprise_recommend", createIndex = false)
public class InvestmentEnterpriseRecommendPO extends BaseSearchPO {

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
     * 入驻机构id
     */
    private Long orgId;

    /**
     * 入驻机构名称
     */
    private String orgName;

    /**
     * 入驻机构code
     */
    private String orgCode;

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
     * 推荐日期
     */
    private Long recommendedDate;

    /**
     * 推荐理由详细信息
     */
    private String recommendationReasonDetail;

    /**
     * 审核状态 0待审核 1已发布 2未通过 3已下线
     */
    private Integer auditStatus;

    /**
     * 批次号
     */
    private String batchNumber;

    /**
     * 推荐类型：0后台添加 1亲商招商 2资源招商 3链主招商 4政策招商 5舆情招商 6AI+招商
     */
    private Integer type;

    /**
     * 关联亲商姓名
     */
    private String relationUserName;

    /**
     * 关联亲商姓名唯一标识
     */
    private String relationUserNameOnlyLogo;

    /**
     * 亲商模式推荐关联关系
     */
    private String associationRelationship;

    /**
     * 资源需求
     */
    private String resourceNeeds;

    /**
     * 关联本地企业社会统一信用代码
     */
    private String associateLocalEnterpriseCode;

    /**
     * 关联本地企业名称
     */
    private String associateLocalEnterpriseName;

    /**
     * 供应关系
     */
    private String supplyRelation;

    /**
     * 关联政策
     */
    private String associatePolicy;

    /**
     * 关联资讯url
     */
    private String associateInformationUrl;

    /**
     * 对外投资意愿
     */
    private String outsideInvestSatisfaction;

    /**
     * 企业关联二级节点id集合
     */
    private List<Long> secondChainNodeIds;

    /**
     * 企业关联产业链二级节点名称集合
     */
    private List<String> secondChainNodeNames;

    public static InvestmentEnterpriseRecommendPO adapt(InvestmentEnterpriseRecommendDO param) {
        InvestmentEnterpriseRecommendPO result = BeanUtil.copyProperties(param, InvestmentEnterpriseRecommendPO.class);
        LocalDateTime recommendedDate = param.getRecommendedDate();
        if (Objects.nonNull(recommendedDate)) {
            result.setRecommendedDate(DateUtil.getTimestamp(recommendedDate));
        }
        result.setGmtCreate(DateUtil.getTimestamp(param.getGmtCreate()));
        result.setGmtModify(DateUtil.getTimestamp(param.getGmtModify()));
        return result;
    }
}
