package cn.idicc.taotie.service.search;

import cn.idicc.taotie.infrastructment.po.data.InformationPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: wd
 * @Date: 2023/4/18
 * @Description:资讯
 * @version: 1.0
 */
@Repository
public interface InformationSearch extends ElasticsearchRepository<InformationPO, Long> {

}
