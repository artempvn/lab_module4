package com.epam.esm.dao.impl;

import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

  private final HibernateSessionFactoryUtil sessionFactory;

  //  @PersistenceContext
  //  EntityManager entityManager;
  //
  //  protected Session getCurrentSession()  {
  //    return entityManager.unwrap(Session.class);
  //  }

  public TagDaoImpl(HibernateSessionFactoryUtil sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Tag create(Tag tag) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.save(tag);
    session.close();
    return tag;
  }

  @Override
  public Optional<Tag> read(long id) {
    Session session = sessionFactory.getSessionFactory().openSession();
    Optional<Tag> tag = Optional.ofNullable(session.get(Tag.class, id));
    session.close();
    return tag;
  }

  @Override
  public List<Tag> readAll() {
    Session session = sessionFactory.getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
    criteria.from(Tag.class);
    List<Tag> tags = session.createQuery(criteria).list();
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
