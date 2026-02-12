package cn.idicc.taotie.infrastructment.po.data;

import cn.hutool.json.JSONObject;
import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseDO;
import cn.idicc.taotie.infrastructment.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Objects;

/**
 * @Author: WangZi
 * @Date: 2023/4/18
 * @Description:
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "enterprise", createIndex = false)
public class EnterprisePO extends BaseSearchPO {

    /**
     * 状态 0:禁用 1:启用
     */
    private Boolean status;

    /**
     * 登记状态
     */
    private String registerStatus;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 纳税人识别号
     */
    private String taxpayerIdentificationNumber;

    /**
     * 注册号
     */
    private String registrationNumber;

    /**
     * 组织机构代码
     */
    private String organizeCode;

    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 实缴资本
     */
    private String paidUpCapital;

    /**
     * 参保人数
     */
    private String insuredPersonsNumber;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 上市类型
     */
    private String typeOfListing;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 成立日期
     */
    private Long registerDate;

    /**
     * 核准日期
     */
    private String approveDate;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String area;

    /**
     * 企业地址
     */
    private String enterpriseAddress;

    /**
     * 最新年报地址
     */
    private String addressOfLatestAnnualReport;

    /**
     * 企业类型
     */
    private String enterpriseType;

    /**
     * 企业评分
     */
    private Integer scoreOfEnterprise;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 更多电话
     */
    private String moreMobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 更多邮箱
     */
    private String moreEmail;

    /**
     * 网址
     */
    private String website;

    /**
     * 国标行业门类
     */
    private String nationalStandardIndustry;

    /**
     * 国标行业门类id
     */
    private Long nationalStandardIndustryId;

    /**
     * 国标行业大类
     */
    private String nationalStandardIndustryBig;

    /**
     * 国标行业大类id
     */
    private Long nationalStandardIndustryBigId;

    /**
     * 国标行业中类
     */
    private String nationalStandardIndustryMiddle;

    /**
     * 国标行业中类id
     */
    private Long nationalStandardIndustryMiddleId;

    /**
     * 国标行业小类
     */
    private String nationalStandardIndustrySmall;

    /**
     * 国标行业小类id
     */
    private Long nationalStandardIndustrySmallId;

    /**
     * 是否拥有专利 0否1是
     */
    private Boolean havaPatent;

    /**
     * 经营状态
     */
    private String operatingStatus;

    /**
     * 企业规模
     */
    private String scale;

    /**
     * 企业简介
     */
    private String introduction;

    /**
     * 关联产业链二级节点id集合
     */
    private List<Long> secondChainNodeIds;

    /**
     * 关联产业链二级节点名称集合
     */
    private List<String> secondChainNodeNames;

    /**
     * 关联企业标签id集合
     */
    private List<Long> enterpriseLabelIds;

    /**
     * 关联产业链标签id集合
     */
    private List<Long> industryLabelIds;

    /**
     * 关联的产业链id集合
     */
    private List<Long> chainIds;

    /**
     * 注册资本转化单位为元后的值
     */
    private String registeredCapitalYuan;

    /**
     * 是否招商企业
     */
    private Boolean haveInvestmentEnterprise;

    /**
     * 推荐日期
     */
    private Long recommendedDate;
    /**
     * 节点评分
     */
    private JSONObject nodeScore;
    /**
     * 关注人id集合
     */
    private List<UserCollectInfo> userCollectInfo;
    /**
     * 龙头企业
     */
    private LeadingEnterprise leadingEnterprise;
    /**
     * 产业企业研发投入总额及年份数据 包含研发投入年份、研发投入金额字段
     */
    private List<InvestmentProportionAmount> investmentProportionAmounts;


    /**
     * 节点IDs
     */
    private List<Long> nodeIds;

    /**
     * 籍贯code，每一个code存三份，分别为AABBCC,AA0000,AABB00
     */
    private List<String> ancestorCode;

    /**
     * 学校code，每一个code存三份，分别为AABBCC,AA0000,AABB00
     */
    private List<String> academicCode;

    /**
     * 对应学校Md5
     */
    private List<String> academicMd5s;

    /**
     * 成长指数
     */
    private Double growthIndex;

    /**
     * 扩张指数
     */
    private Double expansionIndex;

    /**
     * 该企业关联的校友会更新最新时间
     * */
    private Long alumniGmtModify;


    public static EnterprisePO adapt(EnterpriseDO param) {
        EnterprisePO enterprisePO = BeanUtil.copyProperties(param, EnterprisePO.class);
        if (Objects.nonNull(param.getGmtCreate())){
            enterprisePO.setGmtCreate(DateUtil.getTimestamp(param.getGmtCreate()));
        }
        if (Objects.nonNull(param.getGmtModify())){
            enterprisePO.setGmtModify(DateUtil.getTimestamp(param.getGmtModify()));
        }
        return enterprisePO;
    }
}
