package cn.idicc.taotie.infrastructment.po.data;

import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentEnterpriseDO;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Objects;

/**
 * @Author: WangZi
 * @Date: 2023/5/10
 * @Description: 招商企业es索引实体类
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "investment_enterprise", createIndex = false)
public class InvestmentEnterprisePO extends BaseSearchPO {

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
     * 入驻机构code
     */
    private String orgCode;

    /**
     * 入驻机构名称
     */
    private String orgName;

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
     * 分配状态 0未分配 1跟进中 2已失效
     */
    private Integer allocationState;

    /**
     * 失效时间
     */
    private Long failureTime;

    /**
     * 是否完结
     */
    private Boolean isEnd;

    /**
     * 企业关联产业链id集合
     */
    private List<Long> chainIds;

    /**
     * 企业关联企业标签id集合
     */
    private List<Long> enterpriseLabelIds;

    /**
     * 企业关联产业链标签id集合
     */
    private List<Long> industryLabelIds;

    /**
     * 企业关联二级节点id集合
     */
    private List<Long> secondChainNodeIds;

    /**
     * 企业关联产业链二级节点名称集合
     */
    private List<String> secondChainNodeNames;

    /**
     * 企业成立日期
     */
    private Long registerDate;

    /**
     * 企业注册资本
     */
    private String registeredCapital;

    /**
     * 企业参保人数
     */
    private String insuredPersonsNumber;

    /**
     * 企业地址
     */
    private String enterpriseAddress;

    /**
     * 推荐模型集合
     */
    private List<Integer> modelTypes;


    public static InvestmentEnterprisePO adapt(InvestmentEnterpriseDO param) {
        InvestmentEnterprisePO result = BeanUtil.copyProperties(param, InvestmentEnterprisePO.class);
        if (Objects.nonNull(param.getFailureTime())) {
            result.setFailureTime(DateUtil.getTimestamp(param.getFailureTime()));
        }
        result.setRecommendedDate(DateUtil.getTimestamp(param.getRecommendedDate()));
        result.setGmtCreate(DateUtil.getTimestamp(param.getGmtCreate()));
        result.setGmtModify(DateUtil.getTimestamp(param.getGmtModify()));
        return result;
    }
}
