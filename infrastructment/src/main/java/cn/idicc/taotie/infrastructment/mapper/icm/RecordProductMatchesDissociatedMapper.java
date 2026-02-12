package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordProductMatchesDissociatedDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesDissociatedDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


public interface RecordProductMatchesDissociatedMapper extends BaseMapper<RecordProductMatchesDissociatedDO> {


	Integer insertBatch(@Param("list") List<RecordProductMatchesDissociatedDO> insertList);

	IPage<RecordProductMatchesDissociatedDTO> pageList(@Param("page") IPage<RecordProductMatchesDissociatedDTO> page,
													   @Param("chainId") Long chainId,
													   @Param("enterpriseName") String enterpriseName,
													   @Param("productName") String productName,
													   @Param("status") Integer status,
													   @Param("labelId") Long labelId,
													   @Param("nodeId") Long nodeId,
													   @Param("minMatchedScore") BigDecimal minMatchedScore);

}
