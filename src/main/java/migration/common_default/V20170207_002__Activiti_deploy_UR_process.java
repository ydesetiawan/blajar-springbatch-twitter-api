/* Copyright (C) 2014 ASYX International B.V. All rights reserved. */
package migration.common_default;

import java.sql.Connection;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * @author Frank
 * @version 1.0, Aug 13, 2014
 * @since 3.2.0
 */
public class V20170207_002__Activiti_deploy_UR_process implements JdbcMigration {

    private static Logger log = LoggerFactory
            .getLogger(V20170207_002__Activiti_deploy_UR_process.class);

    private static final String PROCESS = "activiti/UserRegistrationProcess.bpmn";

    @Override
    public void migrate(Connection connection) throws Exception {
        SingleConnectionDataSource ds = new SingleConnectionDataSource(
                connection, true);
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .setDataSource(ds)
                .setDatabaseSchemaUpdate(
                        ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJobExecutorActivate(false).buildProcessEngine();
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        String deploymentId = repositoryService.createDeployment()
                .addClasspathResource(PROCESS).deploy().getId();
        log.info("Process deployed: " + deploymentId);
        processEngine.close();
        DataSourceUtils.releaseConnection(connection, ds);
        connection.setAutoCommit(false);
    }
}
