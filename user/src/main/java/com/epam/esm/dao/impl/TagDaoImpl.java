package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.TagDto;
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
  public TagDto create(TagDto tagDto) {
    Tag tag = new Tag(tagDto);
    entityManager.persist(tag);
    return new TagDto(tag);
  }

  @Override
  public Optional<TagDto> read(String name) {
    String hql = "from  Tag where name=:name";
    Query query = entityManager.createQuery(hql).setParameter("name", name);
    Optional<Tag> tag = query.getResultStream().findFirst();
    return tag.map(TagDto::new);
  }
}
