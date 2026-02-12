package cn.idicc.taotie.infrastructment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterprisePartnerRelationshipDTO {


        private Long id;

        private Long enterpriseId;

       private List<Long> chainIds;
       private List<RelationshipDTO> relationship;
        private List<String> counterpartUniCodeList;
        private Long LastModifyTime;
        private Long LastCreateTime;
        //关联enterprise表：企业名称
        private String selfEnterpriseName;

        //关联enterprise表：企业联系方式
        private String selfMobile;

        //企业本身社会统一信用代码
        private String selfUniCode;

        //本方企业注册地址
        private String selfAddress;

        //本方企业地区编码
        private String selfRegionCode;

        //企业注册地-省
        private String selfEnterpriseProvince;
        //企业注册地-市
        private String selfEenterpriseCity;
        //企业注册地-区
        private String selfEnterpriseArea;

        /**
         * 企业规模
         */
        private String enterpriseScale;

        /**
         * 建立时间
         */
        private String establishment;

        /**
         * 注册资本
         */
        private String registeredCapital;

        /**
         * 修改时间
         */
        private Date modifyTime;

}


