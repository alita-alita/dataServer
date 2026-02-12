package cn.idicc.taotie.infrastructment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstindustryAssociationDTO {

    /**
     * 协会id
     */
    private Long id;

    /**
     * 协会md5
     */
    private String associationMd5;

    /**
     * 协会名称
     */
    private String associationName;

    /**
     * 协会统一社会信用代码
     */
    private String instindustryAssociationUniCode;

    /**
     * 协会官网网址
     */
    private String registerAddress;

    /**
     * 协会联系方式
     */
    private String mobile;

    /**
     * 区域代码
     */
    private String regionCode;

    private Set<EnterpriseAndChainDTO> enterpriseAndChains;

}
