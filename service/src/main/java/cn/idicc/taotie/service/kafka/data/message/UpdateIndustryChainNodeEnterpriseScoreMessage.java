package cn.idicc.taotie.service.kafka.data.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wd
 * @description 更新节点企业评分message
 * @date 3/16/23 3:47 PM
 */
@Data
public class UpdateIndustryChainNodeEnterpriseScoreMessage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 节点企业评分信息表主键id
     */
    private Long id;
    /**
     * 评分
     */
    private Double enterpriseScore;
}
