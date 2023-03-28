package com.mycompany.myapp.config.custom;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Configuration
public class MasterDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.master.datasource")
    public DataSourceProperties masterDataSourceProperties()
    {
        return new DataSourceProperties();
    }

    @Bean
    @LiquibaseDataSource
    @ConfigurationProperties("spring.master.hikari")
    public DataSource masterDataSource() {
        HikariDataSource dataSource = masterDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
        dataSource.setPoolName("masterDataSource");
        return dataSource;
    }
}
