package cn.idicc.taotie.service.config;

import cn.idicc.taotie.service.handler.DefaultDBFieldHandler;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
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

@Configuration
@MapperScan(basePackages = {"cn.idicc.taotie.infrastructment.mapper.wenchang"}, sqlSessionFactoryRef = "sqlSessionFactory6")
public class SixthDataSourceConfig {

    @Bean(name = "dataSource6")
    @ConfigurationProperties(prefix = "spring.datasource6")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionFactory6")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource6") DataSource dataSource,
                                               MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(mybatisPlusInterceptor);
        GlobalConfig globalConfig=new GlobalConfig();
        globalConfig.setMetaObjectHandler(new DefaultDBFieldHandler());
        globalConfig.setBanner(false);
        bean.setGlobalConfig(globalConfig);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers6/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "transactionManager6")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource6") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate6")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory6") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}


