package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.IndustryChainAtomNodeDO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IndustryChainAtomNodeService extends IService<IndustryChainAtomNodeDO> {

	IndustryChainAtomNodeDO selectOne(String atomNodeName);

	int deleteById(Long id);

	int updateAndRefActions(Long atomNodeId, String atomNodeName, String nodeDesc);

	String highDangerActionConfirm(Long atomNodeId);

}
