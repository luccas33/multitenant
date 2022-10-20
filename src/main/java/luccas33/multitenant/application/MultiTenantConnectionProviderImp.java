package luccas33.multitenant.application;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Component
public class MultiTenantConnectionProviderImp implements MultiTenantConnectionProvider {

    private static final Map<String, DriverManagerDataSource> dataSourceMap = new HashMap<>();

    public MultiTenantConnectionProviderImp() {
        System.out.println("### Creating MultiTenantConnectionProviderImp");
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String s) throws SQLException {
        return getDataSource(s).getConnection();
    }

    @Override
    public void releaseConnection(String s, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }

    public static DriverManagerDataSource getDataSource() {
        return getDataSource("public");
    }

    public static DriverManagerDataSource getDataSource(String schema) {
        schema = schema == null || schema.trim().isEmpty() ? "public" : schema.trim();
        DriverManagerDataSource dataSource = dataSourceMap.get(schema);
        if (dataSource == null) {
            dataSource = createDataSource();
            dataSource.setSchema(schema);
            dataSourceMap.put(schema, dataSource);
        }
        return dataSource;
    }

    private static DriverManagerDataSource createDataSource() {
        System.out.println("### Creating datasource");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Properties props = properties();
        dataSource.setUrl(props.getProperty(AvailableSettings.URL));
        dataSource.setUsername(props.getProperty(AvailableSettings.USER));
        dataSource.setPassword(props.getProperty(AvailableSettings.PASS));
        dataSource.setDriverClassName(props.getProperty(AvailableSettings.DRIVER));
        dataSource.setConnectionProperties(props);
        return dataSource;
    }

    private static Properties properties() {
        Properties props = new Properties();
        try {
            Properties config = new Properties();
            config.load(MultiTenantConnectionProviderImp.class.getResourceAsStream("/application.properties"));
            props.setProperty(AvailableSettings.URL, config.getProperty("spring.datasource.url"));
            props.setProperty(AvailableSettings.USER, config.getProperty("spring.datasource.username"));
            props.setProperty(AvailableSettings.PASS, config.getProperty("spring.datasource.password"));
            props.setProperty(AvailableSettings.DRIVER, config.getProperty("spring.datasource.driver-class-name"));
            props.setProperty(AvailableSettings.DIALECT, config.getProperty("spring.jpa.properties.hibernate.dialect"));
            props.setProperty(AvailableSettings.MULTI_TENANT, config.getProperty("spring.jpa.properties.hibernate.multiTenancy"));
            props.setProperty(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, config.getProperty("spring.jpa.properties.hibernate.multi_tenant_connection_provider"));
            props.setProperty(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, config.getProperty("spring.jpa.properties.hibernate.tenant_identifier_resolver"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

}
