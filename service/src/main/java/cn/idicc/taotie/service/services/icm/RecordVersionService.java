package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.request.icm.ChainStartProducingDTO;
import cn.idicc.taotie.infrastructment.request.icm.RecordVersionCheckRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordVersionQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordVersionDTO;
import cn.idicc.taotie.infrastructment.response.icm.VersionProduceStatusDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/1/3
 * @Description:
 * @version: 1.0
 */
public interface RecordVersionService {


	void chainStartOnline(Long chainId, Boolean isDw, Boolean isChain);

	void chainStartProducing(ChainStartProducingDTO producingDTO);

	void chainStopProducing(String chainId);

	IPage<RecordVersionDTO> pageChainProduceRecords(RecordVersionQueryRequest request);

	IPage<RecordVersionDTO> pageChainOnlineRecords(RecordVersionQueryRequest request);

	List<VersionProduceStatusDTO> getProducingStatus(String chainId);

	void checkExport(RecordVersionCheckRequest request, HttpServletResponse response);
}
