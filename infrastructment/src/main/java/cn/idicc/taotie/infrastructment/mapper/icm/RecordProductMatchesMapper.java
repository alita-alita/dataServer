package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordProductMatchesDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesAiCheckDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 版本状态变更表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-07
 */
public interface RecordProductMatchesMapper extends BaseMapper<RecordProductMatchesDO> {

	IPage<RecordProductMatchesAiCheckDTO> pageList(@Param("page") IPage<RecordProductMatchesAiCheckDTO> page,
												   @Param("chain_id") Long chainId,
												   @Param("enterprise_name") String enterpriseName,
												   @Param("product_name") String productName);

	void insertBatch(@Param("list") List<ProductMatchesDTO> productList, @Param("version") Long chainId);

	void insertBatchIgnore(@Param("list") List<ProductMatchesDTO> productList, @Param("version") Long chainId);


	List<RecordProductMatchesDO> getExistRelationByChainId(@Param("chain_id") Long chainId);

	Integer physicsDeleteByRelation(@Param("chain_id") Long chainId,
									@Param("label_id") Long labelId,
									@Param("node_id") Long nodeId
	);

	List<Long> getExistsChainIds();
}
