package cn.idicc.taotie.service.services.data.knowledge.strategy.context;

import cn.idicc.identity.exception.BizException;
import cn.idicc.taotie.service.services.data.knowledge.strategy.KnowledgeDataSyncStrategy;
import cn.idicc.taotie.service.services.data.knowledge.strategy.config.KnowledgeStrategyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guyongliang
 */
@Slf4j
@Component
public class KnowledgeSyncContext {

	// 注入所有策略实现（Spring会自动将同类型Bean放入Map，key为Bean名称）
	@Autowired
	private Map<String, KnowledgeDataSyncStrategy<?>> strategyMap = new HashMap<>();

	/**
	 * 是否开启知识库同步
	 */
	@Value("${knowledge.sync.enable:true}")
	private Boolean knowledgeSyncEnable;

	// 根据配置获取对应的策略
	private KnowledgeDataSyncStrategy<?> getStrategy(KnowledgeStrategyEnum strategyType) {
		return strategyMap.get(strategyType.getName());
	}

	public <T> void executeSync(KnowledgeStrategyEnum strategyType, T data) {
		try {
			if (!knowledgeSyncEnable) {
				log.warn("知识库同步方法未开启");
				return;
			}
			KnowledgeDataSyncStrategy<T> strategy = (KnowledgeDataSyncStrategy<T>) getStrategy(strategyType);
			if (strategy == null) {
				throw new BizException("未找到对应的同步策略");
			}
			strategy.dataSync(data);
		} catch (Exception e) {
			log.error("知识库同步方法执行异常:", e);
		}
	}

	// 执行同步（对外暴露的方法）
	public <T> void executeSync(KnowledgeStrategyEnum strategyType, List<T> data) {
		try {
			if (!knowledgeSyncEnable) {
				log.warn("知识库同步方法未开启");
				return;
			}
			KnowledgeDataSyncStrategy<T> strategy = (KnowledgeDataSyncStrategy<T>) getStrategy(strategyType);
			if (strategy == null) {
				throw new BizException("未找到对应的同步策略");
			}
			strategy.dataSync(data);
		} catch (Exception e) {
			log.error("知识库同步方法执行异常:", e);
		}
	}
}