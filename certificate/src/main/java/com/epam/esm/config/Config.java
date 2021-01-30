package com.epam.esm.config;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
public class Config {

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

  @Bean
  public SessionFactory getSessionFactory() {
    org.hibernate.cfg.Configuration configuration =
        new org.hibernate.cfg.Configuration().configure();
    configuration.addAnnotatedClass(Tag.class);
    configuration.addAnnotatedClass(Certificate.class);

    return configuration.buildSessionFactory();
  }

  @Bean
  public LocalValidatorFactoryBean getValidator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

  @Bean
  public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver sessionLocaleResolver = new AcceptHeaderLocaleResolver();
    sessionLocaleResolver.setDefaultLocale(Locale.US);
    return sessionLocaleResolver;
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setBasename("classpath:message");
    return messageSource;
  }
}
