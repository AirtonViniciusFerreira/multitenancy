package com.mycompany.myapp.config.custom;

import com.mycompany.myapp.master.domain.TenancyData;
import com.mycompany.myapp.master.repository.TenancyDataRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class TenantDynamicDataSourceConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final String TENANT_POOL_NAME_SUFFIX = "_DataSource";

    @Autowired
    @Qualifier("masterDataSource")
    private DataSource masterDataSource;

    @Autowired
    @Qualifier("masterDataSourceProperties")
    private DataSourceProperties dataSourceProperties;

    @Autowired
    @Qualifier("tenancyDataSourceProperties")
    DataSourceProperties tenancyDataSourceProperties;

    @Autowired
    @Qualifier("tenancyDataSource")
    private DataSource tenancyDataSource;

    @Autowired
    private TenancyDataRepository tenancyDataRepository;

    @Override
    protected DataSource selectAnyDataSource() {
        return masterDataSource;
    }

    @PostConstruct
    private void createCache() {

    }
    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        try {
            return getTenancy(tenantIdentifier);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Falha ao carregar DataSource do locatario: %s1" , tenantIdentifier));
        }
    }

    private DataSource getTenancy(String tenantIdentifier)
    {
        Long id = Long.parseLong(tenantIdentifier);
        return  createAndConfigureDataSource(tenancyDataRepository.getById(id));
    }

    private DataSource createAndConfigureDataSource(TenancyData tenancy) {

        HikariDataSource ds = this.tenancyDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        HikariDataSource dsLocatario = (HikariDataSource) this.tenancyDataSource;

        if (dsLocatario.getMaximumPoolSize() > 0)
            ds.setMaximumPoolSize(dsLocatario.getMaximumPoolSize());
        if (dsLocatario.getMinimumIdle() >= 0)
            ds.setMinimumIdle(dsLocatario.getMinimumIdle());
        if (dsLocatario.getIdleTimeout() >= 0L)
            ds.setIdleTimeout(dsLocatario.getIdleTimeout());
        ds.setAutoCommit(dsLocatario.isAutoCommit());

        ds.setUsername(tenancy.getUser());
        ds.setPassword(tenancy.getPassword());
        ds.setJdbcUrl(tenancy.getUrl());

        ds.setPoolName(tenancy.getUser() + "_" + TENANT_POOL_NAME_SUFFIX);

        return ds;
    }
}
