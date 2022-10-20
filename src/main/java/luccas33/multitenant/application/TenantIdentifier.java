package luccas33.multitenant.application;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifier implements CurrentTenantIdentifierResolver {

    private static String currentTenant = "public";

    public TenantIdentifier() {
        System.out.println("### Creating TenantIdentifier");
    }

    public static void setCurrentTenant(String tenant) {
        if (tenant == null || tenant.trim().isEmpty()) {
            return;
        }
        currentTenant = tenant.trim();
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        System.out.println("### TenantIdentifier.resolveCurrentTenantIdentifier()");
        return currentTenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
