package com.example.demo.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DatabaseConfig {

  private static final String JPA_PACKAGE_TO_SCAN = "com.example.demo";
  private static final String DIALECT = "org.hibernate.dialect.HSQLDialect";

  /**
   * Configures the datasource.
   *
   * @return an embedded HSQL data source
   */
  @Bean
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    return builder
        .setType(EmbeddedDatabaseType.HSQL)
        .addScript("sql/create-db.sql")
        .addScript("sql/insert-data.sql")
        .build();
  }

  /**
   * Configures JPA.
   *
   * @return an entity manager factory
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    var entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setDataSource(dataSource());
    entityManagerFactory.setPackagesToScan(JPA_PACKAGE_TO_SCAN);

    // Set JPA properties
    var jpaProperties = new Properties();
    jpaProperties.put(AvailableSettings.DIALECT, DIALECT);
    entityManagerFactory.setJpaProperties(jpaProperties);

    // Indicates to Spring bean that we use Hibernate specificities for JPA implementation
    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

    return entityManagerFactory;
  }
}
