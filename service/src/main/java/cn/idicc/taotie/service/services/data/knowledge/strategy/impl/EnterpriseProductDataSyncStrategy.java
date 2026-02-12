package cn.idicc.taotie.service.services.data.knowledge.strategy.impl;

import cn.idicc.taotie.infrastructment.dao.knowledge.KnowledgeEnterpriseProductRepository;
import cn.idicc.taotie.infrastructment.po.knowledge.KnowledgeEnterpriseProductPO;
import cn.idicc.taotie.service.services.data.knowledge.strategy.KnowledgeDataSyncStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guyongliang
 * @date 2025/11/6
 */
@Slf4j
@Service
public class EnterpriseProductDataSyncStrategy implements KnowledgeDataSyncStrategy<KnowledgeEnterpriseProductPO> {


	@Autowired
	private KnowledgeEnterpriseProductRepository knowledgeEnterpriseProductRepository;

	@Override
	public void dataSync(KnowledgeEnterpriseProductPO knowledgeEnterpriseProductPO) {
		try {
			log.info("同步企业产品信息(知识库)");
		} catch (Exception e) {
			log.error("同步企业产品信息中(知识库)：{}", e.getMessage());
		}

	}

	@Override
	public void dataSync(List<KnowledgeEnterpriseProductPO> enterpriseProductPOS) {
		knowledgeEnterpriseProductRepository.saveAll(enterpriseProductPOS);
	}

}
