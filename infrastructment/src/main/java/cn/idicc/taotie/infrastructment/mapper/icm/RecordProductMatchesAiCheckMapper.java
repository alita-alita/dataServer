package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordProductMatchesAiCheckDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesAiCheckDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 版本状态变更表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-07
 */
public interface RecordProductMatchesAiCheckMapper extends BaseMapper<RecordProductMatchesAiCheckDO> {

	Long pageCount(@Param("chainId") Long chainId,
				   @Param("status") Integer status,
				   @Param("labelId") Long labelId,
				   @Param("nodeId") Long nodeId,
				   @Param("enterprise_name") String enterpriseName,
				   @Param("product_name") String productName);

	List<RecordProductMatchesAiCheckDTO> pageList(@Param("start") long start,
												   @Param("size") long size,
												   @Param("chainId") Long chainId,
												   @Param("status") Integer status,
												   @Param("labelId") Long labelId,
												   @Param("nodeId") Long nodeId,
												   @Param("enterprise_name") String enterpriseName,
												   @Param("product_name") String productName);


	void insertBatch(@Param("list") List<RecordProductMatchesAiCheckDO> productList);

	default Integer updateRecordState(Long id, Integer status) {
		return this.update(null, Wrappers.lambdaUpdate(RecordProductMatchesAiCheckDO.class)
				.set(RecordProductMatchesAiCheckDO::getStatus, status)
				.eq(RecordProductMatchesAiCheckDO::getId, id));
	}

	default Integer updateAllRecordState(long chainId, Integer oldStatus, Integer newStatus) {
		return this.update(null, Wrappers.lambdaUpdate(RecordProductMatchesAiCheckDO.class)
				.set(RecordProductMatchesAiCheckDO::getStatus, newStatus)
				.eq(RecordProductMatchesAiCheckDO::getChainId, chainId)
				.eq(RecordProductMatchesAiCheckDO::getStatus, oldStatus));
	}

	Integer deleteByChainIdAndProductId(@Param("chainIds") Set<Long> chainIds,
										@Param("productIds") Set<String> productIds);

}
