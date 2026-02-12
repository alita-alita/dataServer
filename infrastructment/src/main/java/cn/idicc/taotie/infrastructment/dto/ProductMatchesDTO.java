package cn.idicc.taotie.infrastructment.dto;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordProductMatchesAiCheckDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordProductMatchesDO;
import cn.idicc.taotie.infrastructment.utils.MD5Util;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductMatchesDTO {

    private String productId;
    private String productName;
    private String productUrl;
    private String productPurpose;
    private String productDescription;
    private String extraReason;
    private String enterpriseId;
    private String enterpriseName;
    private String enterpriseUniCode;
    private String industryLabelId;
    private String industryLabelName;
    private String matchedProductScore;
    private String industryChainNodeId;
    private String industryChainNodeName;
    private String industryChainId;
    private String dataSource = "知识库召回";
    private Integer status;
    private Integer deleted = 0;

    //other
    private String checkReason;

    public String getProductLabelRelation() {
        return MD5Util.getMd5Id(industryLabelId + productId);
    }

    public String getEnterpriseLabelRelationId() {
        return MD5Util.getMd5Id(industryLabelId + enterpriseId);
    }

    public String getEnterpriseChainRelationId() {
        return MD5Util.getMd5Id(industryChainId + industryChainNodeId + enterpriseId);
    }

    public RecordProductMatchesAiCheckDO toAiCheckDO(){
        RecordProductMatchesAiCheckDO aiCheckDO = new RecordProductMatchesAiCheckDO();
        aiCheckDO.setChainId(Long.valueOf(industryChainId));
        aiCheckDO.setEnterpriseId(enterpriseId);
        aiCheckDO.setEnterpriseName(enterpriseName);
        aiCheckDO.setEnterpriseUniCode(enterpriseUniCode);
        aiCheckDO.setProductId(productId);
        aiCheckDO.setProductName(productName);
        aiCheckDO.setProductUrl(productUrl);
        aiCheckDO.setProductDescription(productDescription);
        aiCheckDO.setProductPurpose(productPurpose);
        aiCheckDO.setLabelId(Long.valueOf(industryLabelId));
        aiCheckDO.setLabelName(industryLabelName);
        aiCheckDO.setNodeId(Long.valueOf(industryChainNodeId));
        aiCheckDO.setNodeName(industryChainNodeName);
        aiCheckDO.setMatchedScore(new BigDecimal(matchedProductScore));
        aiCheckDO.setMatchReason(extraReason);
        aiCheckDO.setStatus(status);
        return aiCheckDO;
    }

    public static ProductMatchesDTO fromAiCheckDO(RecordProductMatchesAiCheckDO aiCheckDO){
        ProductMatchesDTO dto = new ProductMatchesDTO();
        dto.setProductId(aiCheckDO.getProductId());
        dto.setProductName(aiCheckDO.getProductName());
        dto.setProductUrl(aiCheckDO.getProductUrl());
        dto.setProductDescription(aiCheckDO.getProductDescription());
        dto.setProductPurpose(aiCheckDO.getProductPurpose());
        dto.setExtraReason(aiCheckDO.getMatchReason());
        dto.setCheckReason(aiCheckDO.getCheckReason());
        dto.setEnterpriseId(aiCheckDO.getEnterpriseId());
        dto.setEnterpriseName(aiCheckDO.getEnterpriseName());
        dto.setEnterpriseUniCode(aiCheckDO.getEnterpriseUniCode());
        dto.setIndustryLabelId(String.valueOf(aiCheckDO.getLabelId()));
        dto.setIndustryLabelName(aiCheckDO.getLabelName());
        dto.setMatchedProductScore(aiCheckDO.getMatchedScore().toString());
        dto.setIndustryChainId(aiCheckDO.getChainId().toString());
        dto.setIndustryChainNodeId(aiCheckDO.getNodeId().toString());
        dto.setIndustryChainNodeName(aiCheckDO.getNodeName());
        return dto;
    }

    public static ProductMatchesDTO fromMatchesDO(RecordProductMatchesDO aiCheckDO){
        ProductMatchesDTO dto = new ProductMatchesDTO();
        dto.setProductId(aiCheckDO.getProductId());
        dto.setProductName(aiCheckDO.getProductName());
        dto.setProductUrl(aiCheckDO.getProductUrl());
        dto.setProductDescription(aiCheckDO.getProductDescription());
        dto.setProductPurpose(aiCheckDO.getProductPurpose());
        dto.setExtraReason(aiCheckDO.getMatchReason());
        dto.setCheckReason(aiCheckDO.getCheckReason());
        dto.setEnterpriseId(aiCheckDO.getEnterpriseId());
        dto.setEnterpriseName(aiCheckDO.getEnterpriseName());
        dto.setEnterpriseUniCode(aiCheckDO.getEnterpriseUniCode());
        dto.setIndustryLabelId(String.valueOf(aiCheckDO.getLabelId()));
        dto.setIndustryLabelName(aiCheckDO.getLabelName());
        dto.setMatchedProductScore(aiCheckDO.getMatchedScore().toString());
        dto.setIndustryChainId(aiCheckDO.getChainId().toString());
        dto.setIndustryChainNodeId(aiCheckDO.getNodeId().toString());
        dto.setIndustryChainNodeName(aiCheckDO.getNodeName());
        return dto;
    }
}