package luccas33.multitenant.service;

import luccas33.multitenant.application.MultiTenantConnectionProviderImp;
import luccas33.multitenant.application.StatusException;
import luccas33.multitenant.application.TenantIdentifier;
import luccas33.multitenant.application.TokenUtil;
import luccas33.multitenant.model.*;
import luccas33.multitenant.repository.TenantRepository;
import luccas33.multitenant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class TenantService extends BaseService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppVersionService appVersionService;

    public void createTenant(NewTenant newTenant, String tokenStr) throws StatusException {
        newTenant.validate();
        Token token = TokenUtil.readToken(tokenStr);
        User user;
        try {
            user = userRepository.findById(token.getUserId()).orElse(null);
        } catch (Exception e) {
            throw logAndThrow("Error finding user", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        if (user == null || !user.getAdm()) {
            throw new StatusException("User not allowed for create tenant", HttpStatus.UNAUTHORIZED);
        }

        Tenant otherTenant;
        try {
            otherTenant = tenantRepository.findByNameLike(newTenant.getName().trim());
        } catch (Exception e) {
            throw logAndThrow("Error finding tenant by name", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        if (otherTenant != null) {
            throw new StatusException("Tenant name alredy in use", HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            otherTenant = tenantRepository.findBySchemaName(newTenant.getSchema().trim());
        } catch (Exception e) {
            throw logAndThrow("Error finding tenant by schema", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        if (otherTenant != null) {
            throw new StatusException("Tenant schema alredy in use", HttpStatus.NOT_ACCEPTABLE);
        }

        AppVersion appVersion = appVersionService.getAppVersion(newTenant.getVersion());
        Tenant tenant = new Tenant();
        tenant.setName(newTenant.getName());
        tenant.setSchemaName(newTenant.getSchema());
        tenant.setUpdated(false);
        tenant.setCreated(false);
        tenant.setAppVersion(appVersion);
        try {
            tenantRepository.save(tenant);
        } catch (Exception e) {
            throw logAndThrow("Error saving new tenant", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        TenantIdentifier.setCurrentTenant(newTenant.getSchema());
        createTenant(tenant);
    }

    public Tenant getTenant(String name) throws StatusException {
        if (name == null || name.trim().isEmpty()) {
            throw new StatusException("Tenant name is required", HttpStatus.NOT_ACCEPTABLE);
        }
        Tenant tenant;
        try {
            tenant = tenantRepository.findByNameLike(name.trim());
        } catch (Exception e) {
            throw logAndThrow("Error finding tenant by name", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        if (tenant == null) {
            throw new StatusException("Tenant not found", HttpStatus.NOT_ACCEPTABLE);
        }
        createTenant(tenant);
        updateTenant(tenant);
        return tenant;
    }

    private void createTenant(Tenant tenant) throws StatusException {
        if (tenant == null
                || tenant.getCreated()
                || tenant.getAppVersion() == null
                || tenant.getAppVersion().getCreateScript() == null
                || tenant.getAppVersion().getCreateScript().trim().isEmpty()) {
            return;
        }
        try {
            if (runScript(tenant.getAppVersion().getCreateScript(), tenant.getSchemaName(), true)) {
                tenant.setCreated(true);
                tenant.setUpdated(true);
                tenantRepository.save(tenant);
            }
        } catch (Exception e) {
            throw logAndThrow("Error creating tenant", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void updateTenant(Tenant tenant) throws StatusException {
        if (tenant == null
            || tenant.getUpdated()
            || tenant.getAppVersion() == null
            || tenant.getAppVersion().getUpdateScript() == null
            || tenant.getAppVersion().getUpdateScript().trim().isEmpty()) {
            return;
        }
        try {
            if (runScript(tenant.getAppVersion().getUpdateScript(), tenant.getSchemaName(), false)) {
                tenant.setUpdated(true);
                tenantRepository.save(tenant);
            }
        } catch (Exception e) {
            throw logAndThrow("Error updating tenant", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private boolean runScript(String script, String schema, boolean createSchema) throws SQLException {
        Connection con = MultiTenantConnectionProviderImp.getDataSource(schema).getConnection();
        if (createSchema) {
            con.createStatement().execute("create schema if not exists " + schema);
        }
        con.setSchema(schema);
        boolean done = false;
        StringBuilder sb = new StringBuilder();
        String lineSeparator = "(\n|\r\n|" + System.getProperty("line.separator") + ")";
        for (String line : script.split(lineSeparator)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            if (line.trim().toUpperCase().startsWith("DELIMITER")) {
                con.createStatement().execute(sb.toString().trim());
                done = true;
                sb = new StringBuilder();
                continue;
            }
            sb.append(line);
            sb.append("\n");
        }
        if (sb.toString().trim().length() > 0) {
            con.createStatement().execute(sb.toString().trim());
            return true;
        }
        return done;
    }

}
