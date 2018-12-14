package com.xxx.notes.configure;

import com.github.pagehelper.PageHelper;
import com.xxx.notes.base.plugin.LikeQueryInterceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import tk.mybatis.spring.annotation.MapperScan;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @ClassName MybatisConfiguration
 * @Description Mybatis配置类
 * @Author l17561
 * @Date 2018/7/13 15:08
 * @Version V1.0
 */
@Configuration
@MapperScan("com.xxx.notes.mapper")
public class MybatisConfiguration {

    @Value("${mybatis.mapper-locations}")
    private String mapperPath;
    @Value("${mybatis.type-aliases-package}")
    private String entityPackage;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, org.apache.ibatis.session.Configuration configuration) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        sqlSessionFactoryBean.setConfiguration(configuration);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //String packageSearchPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperPath));
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(entityPackage);

        return sqlSessionFactoryBean;
    }

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "false");
        //通过设置pageSize=0或者RowBounds.limit = 0就会查询出全部的结果。
        //p.setProperty("pageSizeZero", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        return dataSourceTransactionManager;
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        LikeQueryInterceptor interceptor = new LikeQueryInterceptor();
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        interceptor.setProperties(properties);
        configuration.addInterceptor(interceptor);

        return configuration;
    }

    // insertList 不好使的时候，加这段配置，但是这里好似不需要，加了会报错
    /*@Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){

        Properties properties=new Properties();
        properties.put("mappers","tk.mybatis.mapper.common.Mapper,tk.mybatis.mapper.common.MySqlMapper");

        MapperScannerConfigurer scannerConfigurer=new MapperScannerConfigurer();
        scannerConfigurer.getMapperHelper().setProperties(properties);
        scannerConfigurer.setBasePackage("com.xxx.notes.mapper");
        scannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");

        return scannerConfigurer;
    }*/

}
