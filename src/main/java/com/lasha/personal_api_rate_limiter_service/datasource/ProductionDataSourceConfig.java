package com.lasha.personal_api_rate_limiter_service.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class ProductionDataSourceConfig extends BaseDataSourceConfig {

    @Override
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl(super.url);
        dataSource.setUsername(super.username);
        dataSource.setPassword(super.password);

        if (super.hasCustomPool) {
            dataSource.setMaximumPoolSize(5);
            dataSource.setMinimumIdle(1);
        }

        return dataSource;
    }
}
