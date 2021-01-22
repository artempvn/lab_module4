package com.epam.esm.dao.impl;

import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

  private final HibernateSessionFactoryUtil sessionFactory;

  public TagDaoImpl(HibernateSessionFactoryUtil sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Tag create(Tag tag) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    session.save(tag);
    session.getTransaction().commit();
    session.close();
    return tag;
  }

  @Override
  public Optional<Tag> read(long id) {
    Session session = sessionFactory.getSessionFactory().openSession();
    Tag tag = session.get(Tag.class, id);
    session.close();
    return Optional.ofNullable(tag);
  }

  @Override
  public List<Tag> readAll() {
    Session session = sessionFactory.getSessionFactory().openSession();
    List<Tag> tags = session.createQuery("from Tag ").list();
    session.close();
    return tags;
  }

  @Override
  public int delete(long id) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    String hql = "delete from Tag where id = :id";
    Query q = session.createQuery(hql).setParameter("id", id);
    int result = q.executeUpdate();
    session.getTransaction().commit();
    session.close();
    return result;
  }

  @Override
  public Optional<Tag> read(String name) {
    Session session = sessionFactory.getSessionFactory().openSession();
    String hql = "from  Tag where name=:name";
    Query query = session.createQuery(hql).setParameter("name", name);
    Optional<Tag> tag = query.getResultStream().findFirst();
    session.close();
    return tag;
  }
}
