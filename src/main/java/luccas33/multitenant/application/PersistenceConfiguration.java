package luccas33.multitenant.application;

import java.util.Objects;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/*
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
*/

import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "luccas33.multitenant.repository")
public class PersistenceConfiguration {

    @Autowired
    private Environment environment;

    /*

    @Bean
    public DataSource datasource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        dataSource.setConnectionProperties(properties());
        return dataSource;
    }

    */

/*

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(datasource());
        sessionFactory.setPackagesToScan("luccas33.multitenant.application", "luccas33.multitenant.repository");
        sessionFactory.setHibernateProperties(properties());
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("luccas33.multitenant.application");
        factory.setDataSource(datasource());
        factory.setJpaProperties(properties());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
*/

    private Properties properties() {
        Properties props = new Properties();
        props.setProperty(AvailableSettings.URL, environment.getProperty("spring.datasource.url"));
        props.setProperty(AvailableSettings.USER, environment.getProperty("spring.datasource.username"));
        props.setProperty(AvailableSettings.PASS, environment.getProperty("spring.datasource.password"));
        props.setProperty(AvailableSettings.DRIVER, environment.getProperty("spring.datasource.driver-class-name"));
        props.setProperty(AvailableSettings.DIALECT, environment.getProperty("spring.jpa.properties.hibernate.dialect"));
        props.setProperty(AvailableSettings.CONNECTION_PROVIDER, environment.getProperty("spring.jpa.properties.hibernate.connection.provider_class"));

        /*
        props.setProperty("hibernate.multiTenancyStrategy", "SCHEMA");
        props.setProperty("hibernate.default_schema", "public");
        props.setProperty("hibernate.multi_tenant_connection_provider", "luccas33.multitenant.application.ConnectionProvider");
        props.setProperty("hibernate.tenant_identifier_resolver", "luccas33.multitenant.application.TenantIdentifierResolver");
         */

        return props;
    }

}
