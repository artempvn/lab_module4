package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TagDaoImpl implements TagDao {

  private final SessionFactory sessionFactory;

  public TagDaoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public TagDto create(TagDto tagDto) {
    Tag tag = new Tag(tagDto);
    Session session = sessionFactory.getCurrentSession();
    session.save(tag);
    return new TagDto(tag);
  }

  @Override
  public Optional<TagDto> read(long id) {
    Session session = sessionFactory.getCurrentSession();
    Optional<Tag> tag = Optional.ofNullable(session.get(Tag.class, id));
    return tag.map(TagDto::new);
  }

  @Override
  public List<TagDto> readAll() {
    Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
    criteria.from(Tag.class);
    List<Tag> tags = session.createQuery(criteria).list();
    return tags.stream().map(TagDto::new).collect(Collectors.toList());
  }

  @Override
  public void delete(long id) {
    Session session = sessionFactory.getCurrentSession();
    Optional<Tag> tag = Optional.ofNullable(session.get(Tag.class, id));
    tag.ifPresentOrElse(
        session::delete,
        () -> {
          throw ResourceValidationException.validationWithTagId(id).get();
        });
  }

  @Override
  public Optional<TagDto> read(String name) {
    Session session = sessionFactory.getCurrentSession();
    String hql = "from  Tag where name=:name";
    Query query = session.createQuery(hql).setParameter("name", name);
    Optional<Tag> tag = query.getResultStream().findFirst();
    return tag.map(TagDto::new);
  }
}
