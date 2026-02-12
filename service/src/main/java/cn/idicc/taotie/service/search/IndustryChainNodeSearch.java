package cn.idicc.taotie.service.search;

import cn.idicc.taotie.infrastructment.po.data.IndustryChainNodePO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: wd
 * @Date: 2023/4/18
 * @Description:产业链和节点关系
 * @version: 1.0
 */
@Repository
public interface IndustryChainNodeSearch extends ElasticsearchRepository<IndustryChainNodePO, Long> {
}
