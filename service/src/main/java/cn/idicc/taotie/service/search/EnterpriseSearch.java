package cn.idicc.taotie.service.search;

import cn.idicc.taotie.infrastructment.po.data.EnterprisePO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: WangZi
 * @Date: 2023/4/18
 * @Description:
 * @version: 1.0
 */
@Repository
public interface EnterpriseSearch extends ElasticsearchRepository<EnterprisePO, Long> {
}
