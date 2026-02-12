package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordVersionDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordVersionDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 版本主表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-02
 */
public interface RecordVersionMapper extends BaseMapper<RecordVersionDO> {

	Integer getMaxSortByUk(@Param("business_uk") String businessUk,
						   @Param("business_relation_key") String businessRelationKey
	);

	IPage<RecordVersionDTO> produceChainRecords(@Param("page") IPage<RecordVersionDTO> page,
												@Param("chainId") String chainId
	);

	IPage<RecordVersionDTO> onlineChainRecords(@Param("page") IPage<RecordVersionDTO> page,
											   @Param("chainId") String chainId
	);

	Integer deleteUseless();

	default int updateState(Long id, Integer before, Integer after) {
		return this.update(null, Wrappers.lambdaUpdate(RecordVersionDO.class)
				.set(RecordVersionDO::getState, after)
				.eq(RecordVersionDO::getId, id)
				.eq(RecordVersionDO::getState, before));
	}

	default RecordVersionDO selectByVersion(String version) {
		return this.selectOne(Wrappers.lambdaUpdate(RecordVersionDO.class)
				.eq(RecordVersionDO::getVersion, version));
	}

}
