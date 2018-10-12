package com.xxx.notes.configure;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @ClassName ActivitiConfig
 * @Description activiti配置注入
 * @Author l17561
 * @Date 2018/10/10 15:35
 * @Version V1.0
 */
@Configuration
public class ActivitiConfig {

    @Resource
    private DataSource dataSource;

    @Autowired
    private ResourcePatternResolver resourceLoader;

    /**
     * 初始化配置，将创建28张表
     *
     * @return
     */
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration() {

        ProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setAsyncExecutorActivate(false);
        return configuration;
    }

    @Bean
    public ProcessEngine processEngine() {

        return processEngineConfiguration().buildProcessEngine();
    }

    @Bean
    public RepositoryService repositoryService() {

        return processEngine().getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() {

        return processEngine().getRuntimeService();
    }

    @Bean
    public TaskService taskService() {

        return processEngine().getTaskService();
    }

    @Bean
    public IdentityService identityService() {

        return processEngine().getIdentityService();
    }
}
