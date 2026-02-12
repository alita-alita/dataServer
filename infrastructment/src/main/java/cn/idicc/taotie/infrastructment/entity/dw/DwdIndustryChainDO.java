package cn.idicc.taotie.infrastructment.entity.dw;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
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
 * 产业链信息表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dwd_industry_chain")
public class DwdIndustryChainDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产业链编号
     */
    private String industryChainCode;

    /**
     * 产业链名称
     */
    private String industryChainName;

    /**
     * 产业链名称拼音
     */
    private String industryChainNamePinyin;

    /**
     * 备注
     */
    private String notes;

    /**
     * 产业链简称
     */
    private String formerName;

    /**
     * 产业链icon
     */
    private String icon;

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


    public static DwdIndustryChainDO adapter(RecordIndustryChainDO chainDO){
        DwdIndustryChainDO res = new DwdIndustryChainDO();
        res.setId(chainDO.getBizId().intValue());
        res.setIndustryChainCode(chainDO.getChainCode());
        res.setIndustryChainName(chainDO.getChainName());
        res.setIndustryChainNamePinyin(chainDO.getChainNamePinyin());
        res.setNotes(chainDO.getNotes());
        res.setFormerName(chainDO.getFormerName());
        res.setIcon(chainDO.getIcon());
        res.setDeleted(false);
        res.setGmtCreate(chainDO.getGmtCreate());
        res.setGmtModify(chainDO.getGmtModify());
        res.setGmtCreate(chainDO.getGmtCreate());
        res.setGmtModify(chainDO.getGmtModify());
        res.setDataSource("产业链管理平台");
        return res;
    }
}
