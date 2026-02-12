package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产业链DO
 * @author wd
 */
@TableName("industry_chain")
@Data
@NoArgsConstructor
public class IndustryChainDO extends BaseDO {
	private static final long serialVersionUID = 1L;
	
	/** 产业链代码 */
	@TableField("chain_code")
	private String chainCode;
	/** 产业链名称 */
	@TableField("chain_name")
	private String chainName;
	/** 产业链名称拼音 */
	@TableField("chain_name_pinyin")
	private String chainNamePinyin;
	/** 关联机构数 */
	@TableField("relevance_organize_number")
	private Integer relevanceOrganizeNumber;
	/** 关联企业数 */
	@TableField("relevance_enterprise_number")
	private Integer relevanceEnterpriseNumber;
	/** 备注 */
	@TableField("notes")
	private String notes;
	/** 简称 */
	@TableField("former_name")
	private String formerName;
	@TableField("icon")
	private String icon;

	public IndustryChainDO(String chainCode, String chainName) {
		this.chainCode = chainCode;
		this.chainName = chainName;
	}

	public IndustryChainDO(String chainCode, String chainName, String notes) {
		this.chainCode = chainCode;
		this.chainName = chainName;
		this.notes = notes;
	}


}
