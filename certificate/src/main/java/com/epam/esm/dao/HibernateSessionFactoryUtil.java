package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HibernateSessionFactoryUtil {

  private SessionFactory sessionFactory;

  public SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      Configuration configuration = new Configuration().configure();
      configuration.addAnnotatedClass(Tag.class);
      configuration.addAnnotatedClass(Certificate.class);
      StandardServiceRegistryBuilder builder =
          new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
      sessionFactory = configuration.buildSessionFactory(builder.build());
    }
    return sessionFactory;
  }
}
