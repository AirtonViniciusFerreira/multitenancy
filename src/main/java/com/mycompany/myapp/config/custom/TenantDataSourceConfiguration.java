package com.mycompany.myapp.config.custom;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Configuration
public class TenantDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.tenancy.datasource")
    public DataSourceProperties tenancyDataSourceProperties()
    {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @LiquibaseDataSource
    @ConfigurationProperties("spring.tenancy.hikari")
    public DataSource tenancyDataSource() {
        HikariDataSource dataSource = tenancyDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
        dataSource.setPoolName("tenancyDataSource");
        return dataSource;
    }

}
