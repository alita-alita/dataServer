package cn.idicc.taotie.service.search;

import cn.idicc.taotie.infrastructment.po.data.DataWatchStatPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataWatchStatSearch extends ElasticsearchRepository<DataWatchStatPO, String> {
}
