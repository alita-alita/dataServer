package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAiCheckRecordDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordAiCheckRecordDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 版本状态变更表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-01-07
 */
public interface RecordAiCheckRecordMapper extends BaseMapper<RecordAiCheckRecordDO> {

	IPage<RecordAiCheckRecordDTO> page(@Param("page") IPage<RecordAiCheckRecordDTO> page, @Param("chainId") Long chainId);

	default Integer updateStatus(Long id, Integer status) {
		return this.update(null, Wrappers.lambdaUpdate(RecordAiCheckRecordDO.class)
				.set(RecordAiCheckRecordDO::getStatus, status)
				.eq(RecordAiCheckRecordDO::getId, id));
	}

	default Integer updateCompleteDate(Long id, Date date) {
		return this.update(null, Wrappers.lambdaUpdate(RecordAiCheckRecordDO.class)
				.set(RecordAiCheckRecordDO::getCompleteDate, date)
				.eq(RecordAiCheckRecordDO::getId, id));
	}
}
