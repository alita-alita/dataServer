package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产业链DO
 * @author wd
 */
@TableName("record_industry_chain")
@Data
@NoArgsConstructor
public class RecordIndustryChainDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	/**
	 * 业务主键ID
	 */
	private Long bizId;
	
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

	@TableField("state")
	private Integer state;
	/**
	 * 知识库索引ID
	 */
	@TableField("lib_file_id")
	private String  libFileId;

	public RecordIndustryChainDO(String chainCode, String chainName) {
		this.chainCode = chainCode;
		this.chainName = chainName;
	}

	public RecordIndustryChainDO(String chainCode, String chainName, String notes) {
		this.chainCode = chainCode;
		this.chainName = chainName;
		this.notes = notes;
	}

	public static RecordIndustryChainDO adapter(JSONObject jsonObject){
		RecordIndustryChainDO res = new RecordIndustryChainDO();
		res.setBizId(jsonObject.getLong("id"));
		res.setState(jsonObject.getInteger("state"));
		res.setChainCode(jsonObject.getString("chain_code").isEmpty()?null:jsonObject.getString("chain_code"));
		res.setChainName(jsonObject.getString("chain_name").isEmpty()?null:jsonObject.getString("chain_name"));
		res.setChainNamePinyin(jsonObject.getString("chain_name_pinyin").isEmpty()?null:jsonObject.getString("chain_name_pinyin"));
		res.setRelevanceOrganizeNumber(jsonObject.getInteger("relevance_organize_number"));
		res.setRelevanceEnterpriseNumber(jsonObject.getInteger("relevance_enterprise_number"));
		res.setNotes(jsonObject.getString("notes").isEmpty()?null:jsonObject.getString("notes"));
		res.setFormerName(jsonObject.getString("former_name").isEmpty()?null:jsonObject.getString("former_name"));
		res.setIcon(jsonObject.getString("icon").isEmpty()?null:jsonObject.getString("icon"));
		res.setDeleted(jsonObject.getBoolean("deleted"));
		return res;
	}


}
