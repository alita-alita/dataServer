package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAgentProductMatchesDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 产品关联表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-09
 */
public interface RecordAgentProductMatchesMapper extends BaseMapper<RecordAgentProductMatchesDO> {

	Long productAllCount(@Param("file_id") String fileId);

	Long productMatchCount(@Param("file_id") String fileId);

	Page<ProductMatchesDTO> getResultMatch(Page<?> page,
										   @Param("chainId") Long chainId,
										   @Param("fileId") String fileId,
										   @Param("version") String version,
										   @Param("is_limit_score") Boolean isLimitScore,
										   @Param("label_ids") List<Long> labelIds
	);

	Page<ProductMatchesDTO> getResultMatchPage(Page<?> page,
											   @Param("chainId") Long chainId
	);

	int deleteByChainIdAndMatchProduct(@Param("chainId") Set<Long> chainIds,
									   @Param("labelName") String labelName);

}
