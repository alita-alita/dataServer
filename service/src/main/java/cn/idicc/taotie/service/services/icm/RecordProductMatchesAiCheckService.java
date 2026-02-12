package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAgentProductMatchesDO;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckRecordStateEnum;
import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesAiCheckQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesAiCheckDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/4/2
 * @Description:
 * @version: 1.0
 */
public interface RecordProductMatchesAiCheckService {

	void createAICheckTask(Long chainId, List<RecordAgentProductMatchesDO> matchesDTOS);

	void sendAllFailKafkaByVersion(Long chainId);

	void sendKafkaByChainId(Long chainId);

	Long countByChainId(Long chainId, List<RecordAiCheckRecordStateEnum> stateEnums);

	IPage<RecordProductMatchesAiCheckDTO> pageList(RecordProductMatchesAiCheckQueryRequest request);

	void exportFileList(RecordProductMatchesAiCheckQueryRequest request, HttpServletResponse response);

	void updateRecordState(Long id, RecordAiCheckRecordStateEnum stateEnum);

	void updateAllRecordState(Long chainId, RecordAiCheckRecordStateEnum oldStateEnum, RecordAiCheckRecordStateEnum newStateEnum);

	List<ProductMatchesDTO> selectAllPassData(Long chainId);
}
