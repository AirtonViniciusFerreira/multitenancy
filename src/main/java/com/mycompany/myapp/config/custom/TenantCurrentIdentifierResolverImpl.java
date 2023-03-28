package com.mycompany.myapp.config.custom;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component("tenantCurrentIdentifierResolver")
public class TenantCurrentIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String sLocatarioId = TenantContext.getCurrentTenant();
        if (!StringUtils.isEmpty(sLocatarioId)){
            return sLocatarioId;
        } else {
            return "BOOTSTRAP";
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

