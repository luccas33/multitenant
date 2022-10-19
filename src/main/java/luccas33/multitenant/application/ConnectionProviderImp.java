package luccas33.multitenant.application;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionProviderImp extends DatasourceConnectionProviderImpl {


    private static String currentTenant = "public";
    private static final Object syncObj = new Object();
    private static final List<Connection> connections = new ArrayList<>();

    private static DriverManagerDataSource dataSource;

    public ConnectionProviderImp() {
        System.out.println("### Creating ConnectionProviderImp");
    }

    public static void setCurrentTenant(String tenant) {
        if (tenant == null || tenant.trim().isEmpty()) {
            return;
        }
        synchronized (syncObj) {
            System.out.println("ConnectionProviderImp.setCurrentTenant(): " + tenant);
            currentTenant = tenant;
            for (Connection con : new ArrayList<>(connections)) {
                try {
                    if (!con.isClosed()) {
                        con.setSchema(currentTenant);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dataSource.setSchema(tenant);
        }
    }

    public static DriverManagerDataSource loadDataSource() {
        if (dataSource == null) {
            System.out.println("### Creating datasource");
            dataSource = new DriverManagerDataSource();
            Properties props = properties();
            dataSource.setUrl(props.getProperty(AvailableSettings.URL));
            dataSource.setUsername(props.getProperty(AvailableSettings.USER));
            dataSource.setPassword(props.getProperty(AvailableSettings.PASS));
            dataSource.setDriverClassName(props.getProperty(AvailableSettings.DRIVER));
            dataSource.setConnectionProperties(props);
        }
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        synchronized (syncObj) {
            System.out.println("### ConnectionProviderImp.getConnection()");
            try {
                Connection con = getDataSource().getConnection();
                con.setSchema(currentTenant);
                connections.add(con);
                return con;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public DriverManagerDataSource getDataSource() {
        return loadDataSource();
    }

    private static Properties properties() {
        Properties props = new Properties();
        try {
            Properties config = new Properties();
            config.load(ConnectionProviderImp.class.getResourceAsStream("/application.properties"));
            props.setProperty(AvailableSettings.URL, config.getProperty("spring.datasource.url"));
            props.setProperty(AvailableSettings.USER, config.getProperty("spring.datasource.username"));
            props.setProperty(AvailableSettings.PASS, config.getProperty("spring.datasource.password"));
            props.setProperty(AvailableSettings.DRIVER, config.getProperty("spring.datasource.driver-class-name"));
            props.setProperty(AvailableSettings.DIALECT, config.getProperty("spring.jpa.properties.hibernate.dialect"));
            props.setProperty(AvailableSettings.CONNECTION_PROVIDER, config.getProperty("spring.jpa.properties.hibernate.connection.provider_class"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

}
