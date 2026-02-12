package cn.idicc.taotie.service.search;

import cn.idicc.taotie.infrastructment.po.data.InvestmentEnterpriseRecommendPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: WangZi
 * @Date: 2023/5/8
 * @Description: 招商推荐es操作接口
 * @version: 1.0
 */
@Repository
public interface InvestmentEnterpriseRecommendSearch extends ElasticsearchRepository<InvestmentEnterpriseRecommendPO, Long> {
}
