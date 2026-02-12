package cn.idicc.taotie.infrastructment.response.icm;

import cn.hutool.core.date.DatePattern;
import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产业链DTO
 *
 * @author wd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordIndustryChainDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 产业链id
     */
    private Long id;

    /**
     * 上线版本时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime onlineDate;
    /**
     * 产业链代码
     */
    private String chainCode;
    /**
     * 产业链名称
     */
    private String chainName;
    /**
     * 关联机构数
     */
    private Integer relevanceOrganizeNumber;
    /**
     * 关联企业数
     */
    private Integer relevanceEnterpriseNumber;
    /**
     * 备注
     */
    private String notes;
    /**
     * 简称
     */
    private String formerName;
    /**
     * 编辑时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtModify;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtCreate;

    /**
     * 产业链图标
     */
    private String icon;

    private Long categoryId;

    private String categoryName;

    private String libFileId;

    /**
     * 是否允许编辑
     */
    private Boolean isAllowEdit;

    private Integer state;

    public static RecordIndustryChainDTO adapt(RecordIndustryChainDO param) {
        RecordIndustryChainDTO res = BeanUtil.copyProperties(param, RecordIndustryChainDTO.class);
        res.setId(param.getBizId());
        return res;
    }

}
