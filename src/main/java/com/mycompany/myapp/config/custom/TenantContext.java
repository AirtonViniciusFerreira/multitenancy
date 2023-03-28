package com.mycompany.myapp.config.custom;

public class TenantContext {

    private static final InheritableThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static void setCurrentTenant(String tenant)
    {
        currentTenant.set(tenant);
    }

    public static  String getCurrentTenant() { return  currentTenant.get(); }

    public static void clear() { currentTenant.remove(); }
}
