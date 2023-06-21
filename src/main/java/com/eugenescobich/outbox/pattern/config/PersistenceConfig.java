package com.eugenescobich.outbox.pattern.config;

import com.eugenescobich.outbox.pattern.persistance.jpa.ExtendedJpaRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Persistence config.
 */
@Configuration
@EntityScan(basePackages = "com.eugenescobich.outbox.pattern.persistance.model")
@EnableJpaRepositories(
    basePackages = "com.eugenescobich.outbox.pattern.persistance.repository",
    repositoryBaseClass = ExtendedJpaRepositoryImpl.class
)
public class PersistenceConfig {

}
