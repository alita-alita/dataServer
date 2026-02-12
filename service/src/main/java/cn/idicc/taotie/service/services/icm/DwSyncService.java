package cn.idicc.taotie.service.services.icm;


import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;

import java.util.List;

/**
 * @Author: wangjun
 * @Date: 2025/1/9
 * @Description:
 * @version: 1.0
 */
public interface DwSyncService {

	void dwOnline(RecordIndustryChainDO chainDO);

	List<ProductMatchesDTO> exportMatchesData(Long chainId, Boolean isLimitScore, List<Long> filterLabelIds);

	List<ProductMatchesDTO> exportTypicalData(RecordIndustryChainDO chainDO, List<Long> filterLabelIds);

	List<ProductMatchesDTO> mergeData(List<ProductMatchesDTO> matchesDTOS, List<ProductMatchesDTO> typicalList);
}
