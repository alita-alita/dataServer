package cn.idicc.taotie.service.search;

import cn.idicc.taotie.infrastructment.po.data.InvestmentEntrustTaskPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: WangZi
 * @Date: 2023/5/17
 * @Description: 招商委托任务es搜索接口
 * @version: 1.0
 */
@Repository
public interface InvestmentEntrustTaskSearch extends ElasticsearchRepository<InvestmentEntrustTaskPO, Long> {
}
