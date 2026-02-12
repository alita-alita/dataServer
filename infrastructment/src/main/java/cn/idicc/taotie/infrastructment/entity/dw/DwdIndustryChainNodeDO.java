package cn.idicc.taotie.infrastructment.entity.dw;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 产业链节点信息表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dwd_industry_chain_node")
public class DwdIndustryChainNodeDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产业链ID
     */
    private Integer industryChainId;

    /**
     * 产业链名称
     */
    private String industryChainName;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点间线坐标json串
     */
    private String lineInfo;

    /**
     * y坐标值
     */
    private Double ordinateValue;

    /**
     * x坐标值
     */
    private Double abscissaValue;

    /**
     * 节点环节，1上游，2中游，3下游
     */
    private Integer tache;

    /**
     * 下游节点
     */
    private Integer downNodeId;

    /**
     * 上游节点
     */
    private Integer upNodeId;

    /**
     * 节点虚拟位置
     */
    private String nodeVirtualLocation;

    /**
     * 节点属性
     */
    private String nodeType;

    /**
     * 节点简介
     */
    private String chainNodeIntroduce;

    /**
     * 节点序号
     */
    private Integer nodeOrder;

    /**
     * 节点深度，0为根节点
     */
    private Integer nodeLevel;

    /**
     * 父节点，当节点为根节点时取固定值0.
     */
    private Integer nodeParentId;

    /**
     * 是否是挂载企业节点 0: 不是 1: 是
     */
    private Integer isLeaf;

    /**
     * 企业显示最大数量
     */
    private Integer showMaxNumber;

    /**
     * 逻辑删除标志,1:删除 0:未删除
     */
    @TableLogic(value = "0",delval = "1")
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 最近一次更新时间
     */
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
     * 数据来源
     */
    private String dataSource;

    public static DwdIndustryChainNodeDO adapter(RecordIndustryChainNodeDO chainNodeDO,String chainName){
        DwdIndustryChainNodeDO dwdIndustryChainNodeDO = new DwdIndustryChainNodeDO();
        dwdIndustryChainNodeDO.setId(chainNodeDO.getBizId().intValue());
        dwdIndustryChainNodeDO.setIndustryChainId(chainNodeDO.getChainId().intValue());
        dwdIndustryChainNodeDO.setIndustryChainName(chainName);
        dwdIndustryChainNodeDO.setNodeName(chainNodeDO.getNodeName());
        dwdIndustryChainNodeDO.setLineInfo(chainNodeDO.getLineInfo());
        dwdIndustryChainNodeDO.setOrdinateValue(chainNodeDO.getOrdinateValue());
        dwdIndustryChainNodeDO.setAbscissaValue(chainNodeDO.getAbscissaValue());
//        dwdIndustryChainNodeDO.setTache(chainNodeDO.getTache());
//        dwdIndustryChainNodeDO.setDownNodeId(chainNodeDO.getDownNodeId());
//        dwdIndustryChainNodeDO.setUpNodeId(chainNodeDO.getUpNodeId());
//        dwdIndustryChainNodeDO.setNodeVirtualLocation(chainNodeDO.getNodeVirtualLocation());
//        dwdIndustryChainNodeDO.setNodeType(chainNodeDO.getNodeType());
        dwdIndustryChainNodeDO.setChainNodeIntroduce(chainNodeDO.getNodeDesc());
        dwdIndustryChainNodeDO.setNodeOrder(chainNodeDO.getNodeOrder());
        dwdIndustryChainNodeDO.setNodeLevel(chainNodeDO.getNodeLevel());
        dwdIndustryChainNodeDO.setNodeParentId(chainNodeDO.getNodeParent().intValue());
        dwdIndustryChainNodeDO.setIsLeaf(chainNodeDO.getIsLeaf());
        dwdIndustryChainNodeDO.setShowMaxNumber(chainNodeDO.getShowMaxNumber());
        dwdIndustryChainNodeDO.setDeleted(false);
        dwdIndustryChainNodeDO.setGmtCreate(chainNodeDO.getGmtCreate());
        dwdIndustryChainNodeDO.setGmtModify(chainNodeDO.getGmtModify());
        dwdIndustryChainNodeDO.setCreateBy(chainNodeDO.getCreateBy());
        dwdIndustryChainNodeDO.setUpdateBy(chainNodeDO.getUpdateBy());
        dwdIndustryChainNodeDO.setDataSource("产业链管理平台");
        return dwdIndustryChainNodeDO;
    }

}
