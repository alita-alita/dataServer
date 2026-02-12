package cn.idicc.taotie.infrastructment.request.icm;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author wd
 * @description 保存产业链request
 * @date 12/19/22 10:02 AM
 */
@Data
public class SaveRecordIndustryChainNodeRequest implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 产业链ID
     */
    @NotNull(message = "产业链ID不能为空")
    private Long chainId;
    /**
     * 产业链节点ID
     */
    private Long chainNodeId;
    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 原子节点，是叶子节点时，才会有值
     * */
    private String atomNodeValue;

    /**
     * 节点顺序
     */
    @NotNull(message = "节点顺序不能为空")
    private Integer nodeOrder;
    /**
     * 节点深度
     */
    @NotNull(message = "节点深度不能为空")
    private Integer nodeLevel;
    /**
     * 父节点
     */
    @NotNull(message = "父节点不能为空")
    private Long nodeParent;
    /**
     * 是否挂载企业节点 0：目录节点 1：挂载节点
     */
    @NotNull(message = "是否挂载企业节点不能为空")
    private Integer isLeaf;
    /**
     * 最大显示个数
     */
    private Integer showMaxNumber;
    /**
     * 备注
     */
    private String notes;
    /**
     * x坐标
     */
    private Double abscissaValue;
    /**
     * y坐标
     */
    private Double ordinateValue;
    /**
     * 节点间线坐标json串
     */
    private String lineInfo;


    /**
     * 节点描述
     */
    private String nodeDesc;

    /**
     * 节点产品匹配度阈值
     */
    private Double thresholdScore;
}
