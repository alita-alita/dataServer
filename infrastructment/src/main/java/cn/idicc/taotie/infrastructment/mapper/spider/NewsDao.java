package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NewsDao {

    int countByUKey(@Param("uKey") String uKey);

    int insert(News news);

}
