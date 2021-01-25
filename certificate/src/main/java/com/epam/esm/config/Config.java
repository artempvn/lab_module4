package com.epam.esm.config;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public SessionFactory getSessionFactory() {
    org.hibernate.cfg.Configuration configuration =
        new org.hibernate.cfg.Configuration().configure();
    configuration.addAnnotatedClass(Tag.class);
    configuration.addAnnotatedClass(Certificate.class);
    return configuration.buildSessionFactory();
  }
}
