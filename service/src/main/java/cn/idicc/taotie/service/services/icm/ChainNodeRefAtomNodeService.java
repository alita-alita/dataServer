package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.ChainNodeRefAtomNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ChainNodeRefAtomNodeService extends IService<ChainNodeRefAtomNodeDO> {
	List<RecordIndustryChainDO> findRefChain(Long atomNodeId, Long chainNodeId);

	int deleteByAtomId(Long atomNodeId);
}
