package cn.idicc.taotie.service.search;

import cn.idicc.taotie.infrastructment.po.data.InvestmentEnterprisePO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: WangZi
 * @Date: 2023/5/10
 * @Description: 招商企业es操作接口
 * @version: 1.0
 */
@Repository
public interface InvestmentEnterpriseSearch extends ElasticsearchRepository<InvestmentEnterprisePO, Long> {
}
