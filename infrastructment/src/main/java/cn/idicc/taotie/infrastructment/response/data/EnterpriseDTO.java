package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseDO;
import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2022/12/26
 * @Description:
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseDTO implements Serializable {

    private static final long serialVersionUID = -1728041085920371614L;

    /**
     * 主键id
     */
    private Long id;

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
     * 曾用名
     */
    private String formerName;

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
    private String registerDate;

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
    private String nationalStandardIndustryId;

    /**
     * 国标行业大类
     */
    private String nationalStandardIndustryBig;

    /**
     * 国标行业大类id
     */
    private String nationalStandardIndustryBigId;

    /**
     * 国标行业中类
     */
    private String nationalStandardIndustryMiddle;

    /**
     * 国标行业中类id
     */
    private String nationalStandardIndustryMiddleId;

    /**
     * 国标行业小类
     */
    private String nationalStandardIndustrySmall;

    /**
     * 国标行业小类id
     */
    private String nationalStandardIndustrySmallId;

    /**
     * 是否拥有专利 0否1是
     */
    private Boolean havaPatent;

    /**
     * 逻辑删除标志  0否1是
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtModify;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 企业标签，多个使用英文逗号拼接
     */
    private String enterpriseLabels;

    /**
     * 企业标签列表
     */
    private List<EnterpriseLabelDTO> enterpriseLabelDTOS;

    /**
     * 产业链标签，多个使用英文逗号拼接
     */
    private String industryLabels;

    /**
     * 产业链标签列表
     */
    private List<IndustryLabelDTO> industryLabelDTOS;

    /**
     * 专利数量
     */
    private Integer patentNumber;
    /**
     * 线索id
     */
    private Long clueId;
    /**
     * 线索处理状态
     */
    private Integer clueDealState;
    /**
     * 线索是否委托
     */
    private Boolean entrustOrNot;
    /**
     * 线索指派状态
     */
    private Integer clueState;
    /**
     * 企业360数据
     */
    private String data360;

    /**
     * 用户拥有权限产业链名称和企业关联产业链名称交集集合
     */
    private List<String> chainNameList;

    /**
     * 企业需要展示的图标
     */
    private Integer showLabelType;

    /**
     * 所在环节
     */
    private String linkPlace;

    public static EnterpriseDTO adapt(EnterpriseDO param) {
        EnterpriseDTO result = EnterpriseDTO.builder().build();
        BeanUtil.copyProperties(param, result);
        return result;
    }

    public static EnterpriseDO adapt(EnterpriseDTO param) {
        return BeanUtil.copyProperties(param, EnterpriseDO.class);
    }

    public static EnterpriseDTO adapt(EnterprisePO param) {
        return BeanUtil.copyProperties(param, EnterpriseDTO.class);
    }
}
