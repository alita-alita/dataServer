package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAiCheckRecordDO;
import cn.idicc.taotie.infrastructment.request.icm.RecordAiCheckRecordQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordAiCheckRecordDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author: MengDa
 * @Date: 2025/4/2
 * @Description:
 * @version: 1.0
 */
public interface RecordAiCheckRecordService {

	IPage<RecordAiCheckRecordDTO> pageList(RecordAiCheckRecordQueryRequest request);

	RecordAiCheckRecordDO addRecordAiCheckRecord(Long chainId, String desc);

	void runRecord(Long id);

	void stopRecord(Long id);

	void aiCheckDataOnline(Long id, Boolean isCheck);
}
