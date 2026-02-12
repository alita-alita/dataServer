package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("academia_basic_info")
public class AcademiaBasicInfoDO extends DataSyncBaseDO {

	/**
	 * 学术与教育机构id
	 */
	private String academiaMd5;

	/**
	 * 社会信用代码
	 */
	private String uniCode;

	/**
	 * 学术与教育机构名称
	 */
	private String academiaName;

	/**
	 * 学术与教育机构所在地区行政代码
	 */
	private String academiaRegionCode;

	/**
	 * 学术与教育机构类型：研究机构、大学、高中、初中、小学、幼儿园
	 */
	private String academiaType;

	/**
	 * 排序字段
	 */
	private Integer sort = 0;

}
