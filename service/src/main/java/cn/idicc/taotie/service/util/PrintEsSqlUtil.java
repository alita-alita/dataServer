package cn.idicc.taotie.service.util;

import cn.idicc.taotie.infrastructment.po.data.InvestmentEntrustTaskPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: WangZi
 * @Date: 2023/5/17
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Component
public class PrintEsSqlUtil {

    @Value("${print.es.sql:false}")
    private Boolean printEsSql;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void print(Class<?> classType, NativeSearchQuery build) {
        if (printEsSql) {
            try {
                Method method = ReflectionUtils.findMethod(Class.forName("org.springframework.data.elasticsearch.core.RequestFactory"), "searchRequest", Query.class, Class.class, IndexCoordinates.class);
                Assert.notNull(method, "获取method失败");
                method.setAccessible(true);
                Object o = ReflectionUtils.invokeMethod(method, elasticsearchRestTemplate.getRequestFactory(), build, classType, elasticsearchRestTemplate.getIndexCoordinatesFor(InvestmentEntrustTaskPO.class));

                Field source = ReflectionUtils.findField(Class.forName("org.elasticsearch.action.search.SearchRequest"), "source");
                Assert.notNull(source, "获取source失败");
                source.setAccessible(true);
                Object s = ReflectionUtils.getField(source, o);
                log.info("es执行语句为:{}", s);
            } catch (Exception e) {
                log.error("打印es执行语句出现异常", e);
            }
        }
    }
}
