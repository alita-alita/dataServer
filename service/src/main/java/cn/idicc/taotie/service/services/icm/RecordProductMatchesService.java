package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesAiCheckQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesAiCheckDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author: MengDa
 * @Date: 2025/4/2
 * @Description:
 * @version: 1.0
 */
public interface RecordProductMatchesService {

	Integer updateRecord(RecordProductMatchesAiCheckDTO aiCheckDTO);

	IPage<RecordProductMatchesAiCheckDTO> pageList(RecordProductMatchesAiCheckQueryRequest request);
}
