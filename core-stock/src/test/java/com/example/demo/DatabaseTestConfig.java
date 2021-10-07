package com.example.demo;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@ComponentScan("com.example.demo")
@EnableJpaRepositories("com.example.demo")
public class DatabaseTestConfig {

  private static final String JPA_PACKAGE_TO_SCAN = "com.example.demo";
  private static final String DIALECT = "org.hibernate.dialect.HSQLDialect";
  private static final String HIBERNATE_UPDATE_MODE = "create-drop";

  /**
   * Configures the datasource.
   *
   * @return an embedded HSQL data source
   */
  @Bean
  DataSource dataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
  }

  /**
   * Configures JPA.
   *
   * @return an entity manager factory
   */
  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setDataSource(dataSource());
    entityManagerFactory.setPackagesToScan(JPA_PACKAGE_TO_SCAN);

    // Set JPA properties
    var jpaProperties = new Properties();
    jpaProperties.put(AvailableSettings.DIALECT, DIALECT);
    jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, HIBERNATE_UPDATE_MODE);
    entityManagerFactory.setJpaProperties(jpaProperties);

    // Indicates to Spring bean that we use Hibernate specificities for JPA implementation
    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

    return entityManagerFactory;
  }
}
