package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JavaConfig {

  @Autowired private Environment environment;

  @Bean
  public DataSource getDataSource() {
    DriverManagerDataSource dataSource =
        new DriverManagerDataSource(
            environment.getProperty("url"),
            environment.getProperty("uname"),
            environment.getProperty("password"));
    dataSource.setDriverClassName(environment.getProperty("driver"));
    return dataSource;
  }
}
