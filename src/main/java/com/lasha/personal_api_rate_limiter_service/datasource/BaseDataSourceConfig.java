package com.lasha.personal_api_rate_limiter_service.datasource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public abstract class BaseDataSourceConfig {

    @Value("${db.url}")
    protected String url;

    @Value("${db.username}")
    protected String username;

    @Value("${db.password}")
    protected String password;

    protected final boolean hasCustomPool = true;

    public abstract DataSource dataSource();

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource) {

        return builder.dataSource(dataSource)
                .packages("com.lasha.personal_api_rate_limiter_service.model")
                .persistenceUnit("personalApiRateLimiterService")
                .build();
    }

    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
