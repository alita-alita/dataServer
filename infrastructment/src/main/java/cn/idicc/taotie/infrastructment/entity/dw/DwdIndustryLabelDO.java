package cn.idicc.taotie.infrastructment.entity.dw;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
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
 * 产业标签表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dwd_industry_label")
public class DwdIndustryLabelDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产业标签（产品）
     */
    private String labelName;

    /**
     * 标签类型
     */
    private String labelType;

    /**
     * 产业链id
     */
    private Integer industryChainId;

    /**
     * 产业链名称
     */
    private String industryChainName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
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
     * 逻辑删除标志,1:删除 0:未删除
     */
    @TableLogic(value = "0",delval = "1")
    private Boolean deleted;

    /**
     * 数据来源
     */
    private String dataSource;


    public static DwdIndustryLabelDO adapter(RecordIndustryLabelDO labelDO, Long chainId,String industryChainName){
        DwdIndustryLabelDO res = new DwdIndustryLabelDO();
        res.setId(labelDO.getBizId().intValue());
        res.setLabelName(labelDO.getLabelName());
        res.setLabelType(labelDO.getLabelType().toString());
        res.setIndustryChainId(chainId.intValue());
        res.setIndustryChainName(industryChainName);
        res.setDeleted(false);
        res.setGmtCreate(labelDO.getGmtCreate());
        res.setGmtModify(labelDO.getGmtModify());
        res.setCreateBy(labelDO.getCreateBy());
        res.setUpdateBy(labelDO.getUpdateBy());
        res.setDataSource("产业链管理平台");
        return res;
    }

}
