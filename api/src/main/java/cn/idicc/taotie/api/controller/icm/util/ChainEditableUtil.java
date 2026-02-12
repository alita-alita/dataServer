package cn.idicc.taotie.api.controller.icm.util;

import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainDTO;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 检查产业链状态，是否允许编辑
 */
@Component
public class ChainEditableUtil {

	@Autowired
	private RecordIndustryChainService recordIndustryChainService;

	/**
	 * 校验是否允许编辑
	 */
	public boolean allowEdit(Long chainId) {
		RecordIndustryChainDTO recordIndustryChainDTO = recordIndustryChainService.getByChainId(chainId);
		if (recordIndustryChainDTO == null) {
			return false;
		}
		return !(RecordChainStateEnum.NORMAL.getValue() > recordIndustryChainDTO.getState()
				&& recordIndustryChainDTO.getState() < RecordChainStateEnum.SYNC_PRODUCTION_FINISH.getValue());
	}


}
