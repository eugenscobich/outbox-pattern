package com.eugenescobich.iot.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class TestApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:14")
                .withDatabaseName("db_iot_service")
                .withInitScript("db/db_init.sql")
                .withCommand("postgres", "-c", "log_statement=all");

        postgreSQLContainer.start();
        log.info("Postgres Url: {}", postgreSQLContainer.getJdbcUrl());
        TestPropertyValues.of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl())
                .applyTo(configurableApplicationContext.getEnvironment());
    }

}
