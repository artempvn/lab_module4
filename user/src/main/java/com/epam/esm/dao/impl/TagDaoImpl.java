package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

@Repository
@Transactional
public class TagDaoImpl implements TagDao {

  private final EntityManager entityManager;

  public TagDaoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Tag create(Tag tag) {
    entityManager.persist(tag);
    return tag;
  }

  @Override
  public Optional<Tag> read(String name) {
    String hql = "from  Tag where name=:name";
    Query query = entityManager.createQuery(hql).setParameter("name", name);
    return query.getResultStream().findFirst();
  }
}
