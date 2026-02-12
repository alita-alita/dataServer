package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构产业链DTO
 * @author wd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgIndustryChainRelationDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 机构产业链id */
	private Long id;
	/** 产业链id */
	private Long industryChainId;
	/** 机构名称 */
	private String orgName;
	/** 机构id */
	private Long orgId;
	/** 产业链名称 */
	private String chainName;
	/** 备注 */
	private String notes;
	/** 状态 */
	private Boolean status;
	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private Date gmtCreate;
	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private Date gmtModify;
	/** 产业链图标 */
	private String icon;
}
