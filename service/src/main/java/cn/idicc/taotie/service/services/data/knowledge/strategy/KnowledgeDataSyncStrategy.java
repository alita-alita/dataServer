package cn.idicc.taotie.service.services.data.knowledge.strategy;

import java.util.List;

/**
 * @author guyongliang
 * @date 2025/11/6
 */
public interface KnowledgeDataSyncStrategy<T> {

	default void dataSync(T t) {

	}
	default void dataSync(List<T> t) {
	}
}
