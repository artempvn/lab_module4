package com.epam.esm.dao.impl;

import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TagDaoImpl implements TagDao {

  private final PaginationHandler paginationHandler;
  private final EntityManager entityManager;

  public TagDaoImpl(PaginationHandler paginationHandler, EntityManager entityManager) {
    this.paginationHandler = paginationHandler;
    this.entityManager = entityManager;
  }

  @Override
  public TagDto create(TagDto tagDto) {
    Tag tag = new Tag(tagDto);
    Session session = entityManager.unwrap( Session.class );
    session.save(tag);
    return new TagDto(tag);
  }

  @Override
  public Optional<TagDto> read(long id) {
    Session session = entityManager.unwrap( Session.class );
    Optional<Tag> tag = Optional.ofNullable(session.get(Tag.class, id));
    return tag.map(TagDto::new);
  }

  @Override
  public PageData<TagDto> readAll(PaginationParameter parameter) {
    Session session = entityManager.unwrap( Session.class );
    CriteriaBuilder builder = session.getCriteriaBuilder();

    CriteriaQuery<Tag> criteriaQuery = builder.createQuery(Tag.class);
    Root<Tag> from = criteriaQuery.from(Tag.class);
    CriteriaQuery<Tag> select = criteriaQuery.select(from);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
    countQuery.select(builder.count(countQuery.from(Tag.class)));
    Long numberOfElements = session.createQuery(countQuery).getSingleResult();
    long numberOfPages= paginationHandler.calculateNumberOfPages(numberOfElements,parameter.getSize());

    TypedQuery<Tag> typedQuery = session.createQuery(select);
    paginationHandler.setPageToQuery(typedQuery, parameter);
    List<TagDto> tags = typedQuery.getResultList().stream().map(TagDto::new).collect(Collectors.toList());

    return new PageData<>(parameter.getPage(),numberOfElements,numberOfPages,tags);
  }

  @Override
  public void delete(long id) {
    Session session = entityManager.unwrap( Session.class );
    Optional<Tag> tag = Optional.ofNullable(session.get(Tag.class, id));
    tag.ifPresentOrElse(
        session::delete,
        () -> {
          throw ResourceValidationException.validationWithTagId(id).get();
        });
  }

  @Override
  public Optional<TagDto> read(String name) {
    Session session = entityManager.unwrap( Session.class );
    String hql = "from  Tag where name=:name";
    Query query = session.createQuery(hql).setParameter("name", name);
    Optional<Tag> tag = query.getResultStream().findFirst();
    return tag.map(TagDto::new);
  }
}
