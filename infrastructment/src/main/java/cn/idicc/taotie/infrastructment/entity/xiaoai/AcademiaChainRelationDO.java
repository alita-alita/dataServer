package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 研究机构
 * </p>
 *
 * @author MengDa
 * @since 2024-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("academia_chain_relation")
public class AcademiaChainRelationDO extends DataSyncBaseDO {

	/**
	 * 产业链ID
	 */
	private Long industryChainId;

	/**
	 * 学校Md5
	 */
	private String academiaMd5;

	/**
	 * 专业领域
	 */
	private String field;

}
