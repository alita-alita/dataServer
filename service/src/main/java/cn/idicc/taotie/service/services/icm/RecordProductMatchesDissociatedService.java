package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesDissociatedQueryRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesDissociatedUpdateRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesDissociatedDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/5/7
 * @Description:
 * @version: 1.0
 */
public interface RecordProductMatchesDissociatedService {

	void refreshAiMatchesResult(Long chainId);


	IPage<RecordProductMatchesDissociatedDTO> pageList(RecordProductMatchesDissociatedQueryRequest request);

	void batchIgnore(List<Long> ids);

	void batchNotIgnore(List<Long> ids);

	void manualMount(RecordProductMatchesDissociatedUpdateRequest request);

	void updateProductInfo(RecordProductMatchesDissociatedUpdateRequest request);

	void importData(MultipartFile file, RecordProductMatchesDissociatedQueryRequest request);
}
