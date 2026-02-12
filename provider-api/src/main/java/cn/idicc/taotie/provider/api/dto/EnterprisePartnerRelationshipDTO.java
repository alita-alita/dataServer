package cn.idicc.taotie.provider.api.dto;

import java.util.List;

public class EnterprisePartnerRelationshipDTO {


    private Long enterpriseId;
    private List<Long> chainIds;

    private List<RelationshipVO> relationship;

    private Long lastModifyTime;

    //关联enterprise表：企业名称
    private String enterpriseName;

    //关联enterprise表：企业联系方式
    private String mobile;

    //企业本身社会统一信用代码
    private String uniCode;

    //本方企业注册地址
    private String enterpriseAddress;

    //本方企业地区编码
    private String regionCode;
    //企业本身所在省
    private String enterpriseProvince;
    //企业所在市
    private String enterpriseCity;
    //企业所在区
    private String enterpriseArea;
    /**
     * 企业规模
     */
    private String scale;
    /**
     * 建立时间
     */
    private Long establishment;
    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 融资轮次
     */
    private List<Long> financingRounds;
    /**
     * 上市板块
     */
    private List<Long> listedSector;
    /**
     * 科技标签
     */
    private List<Long> technologicalInnovation;

    private static class RelationshipVO {
        private String counterpartRegionCode;
        private Long hisRelationshipType;
        private String hisRelationshipDegree;
    }

}
