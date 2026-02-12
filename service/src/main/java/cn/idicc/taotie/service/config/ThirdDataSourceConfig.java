package cn.idicc.taotie.service.config;

import cn.idicc.taotie.service.handler.DefaultDBFieldHandler;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Author: MengDa
 * @Date: 2023/8/14
 * @Description:
 * @version: 1.0
 */
@Configuration
@MapperScan(basePackages = {"cn.idicc.taotie.infrastructment.mapper.icm"}, sqlSessionFactoryRef = "sqlSessionFactory3")
public class ThirdDataSourceConfig {

    @Bean(name = "dataSource3")
    @ConfigurationProperties(prefix = "spring.datasource3")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionFactory3")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource3") DataSource dataSource,
                                               MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(mybatisPlusInterceptor);
        GlobalConfig globalConfig=new GlobalConfig();
        globalConfig.setMetaObjectHandler(new DefaultDBFieldHandler());
        globalConfig.setBanner(false);
        bean.setGlobalConfig(globalConfig);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers3/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "transactionManager3")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource3") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate3")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory3") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}


