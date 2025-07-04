package com.lasha.personal_api_rate_limiter_service.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
@PropertySource("classpath:application-dev.properties")
public class DevelopmentDataSourceConfig extends BaseDataSourceConfig {

    @Override
    @Bean
    public DataSource dataSource() {
        System.out.println("DevelopmentDataSourceConfig dataSource() called: " + super.url);
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl(super.url);
        dataSource.setUsername(super.username);
        dataSource.setPassword(super.password);

        if(super.hasCustomPool == true) {
            dataSource.setMaximumPoolSize(5);
            dataSource.setMinimumIdle(1);
        }

        return dataSource;
    }
}
