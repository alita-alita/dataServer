package cn.idicc.taotie.infrastructment.entity.xiaoai;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("talent_education_experience")
public class TalentEducationExperienceDO extends DataSyncBaseDO {

	/**
	 * 学校
	 */
	//TODO 修改1218
	private String academiaMd5;

	/**
	 * 人才
	 */
	private String talentMd5;
}
