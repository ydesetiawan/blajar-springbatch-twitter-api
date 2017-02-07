package com.yd.config;

import static org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE;

import java.util.Arrays;

import javax.sql.DataSource;

import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.jobexecutor.DefaultJobExecutor;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;

import com.yd.security.SpringSecurityGroupManagerFactory;
import com.yd.security.SpringSecurityUserManagerFactory;

/**
 * @author edys
 * @version 1.0, May 23, 2014
 * @since 3.1.0
 */
@Configuration
public class ActivitiConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    ActivitiEventListener activitiEventListener;

    @Bean
    public JobExecutor jobExecutor() {
        return new DefaultJobExecutor();
    }

    @Bean
    public ProcessEngineFactoryBean processEngine() {
        ProcessEngineFactoryBean engine = new ProcessEngineFactoryBean();
        engine.setProcessEngineConfiguration(springProcessEngineConfiguration());
        return engine;
    }

    @Bean
    @DependsOn("flyway")
    public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setTransactionManager(transactionManager);
        config.setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_FALSE);
        config.setJobExecutorActivate(false);
        config.setJobExecutor(jobExecutor());
        config.setCustomSessionFactories(Arrays.asList(
                springSecurityUserManagerFactory(),
                springSecurityGroupManagerFactory()));
        config.setEventListeners(Arrays.asList(activitiEventListener));
        return config;
    }

    @Bean
    public SpringSecurityGroupManagerFactory springSecurityGroupManagerFactory() {
        return new SpringSecurityGroupManagerFactory();
    }

    @Bean
    public SpringSecurityUserManagerFactory springSecurityUserManagerFactory() {
        return new SpringSecurityUserManagerFactory();
    }
}
