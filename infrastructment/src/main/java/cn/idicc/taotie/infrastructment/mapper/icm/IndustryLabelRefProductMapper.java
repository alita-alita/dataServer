package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import cn.idicc.taotie.infrastructment.entity.icm.IndustryLabelRefProductDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface IndustryLabelRefProductMapper extends BaseMapper<IndustryLabelRefProductDO> {

	List<ProductMatchesDTO> selectMatchedProducts(
			@Param("chainId") Long chainId,
													@Param("industryLabelIds") Set<Long> industryLabelIds,
												  @Param("start") int start,
												  @Param("size") int size);
}
