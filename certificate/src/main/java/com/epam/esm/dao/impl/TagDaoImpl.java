package com.epam.esm.dao.impl;

import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.exception.ResourceException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

  private final HibernateSessionFactoryUtil sessionFactory;

  public TagDaoImpl(HibernateSessionFactoryUtil sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Tag create(TagDto tagDto) {
    Tag tag = new Tag(tagDto);
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      session.save(tag);
    }
    return tag;
  }

  @Override
  public Optional<Tag> read(long id) {
    Optional<Tag> tag = Optional.empty();
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      tag = Optional.ofNullable(session.get(Tag.class, id));
    }
    return tag;
  }

  @Override
  public List<Tag> readAll() {
    List<Tag> tags = Collections.emptyList();
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
      criteria.from(Tag.class);
      tags = session.createQuery(criteria).list();
    }
    return tags;
  }

  @Override
  public void delete(long id) {
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      Optional<Tag> tag = Optional.ofNullable(session.get(Tag.class, id));
      tag.ifPresentOrElse(
          session::delete,
          () -> {
            throw ResourceException.validationWithTagId(id).get();
          });
      session.getTransaction().commit();
    }
  }

  @Override
  public Optional<Tag> read(String name) {
    Optional<Tag> tag = Optional.empty();
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      String hql = "from  Tag where name=:name";
      Query query = session.createQuery(hql).setParameter("name", name);
      tag = query.getResultStream().findFirst();
    }
    return tag;
  }
}
