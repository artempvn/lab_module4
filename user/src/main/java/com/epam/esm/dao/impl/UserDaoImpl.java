package com.epam.esm.dao.impl;

import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.TagException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserDaoImpl implements UserDao {

  private static final String SQL_REQUEST_FOR_USER_ID_WITH_HIGHEST_COST_ORDERS =
      "(SELECT id "
          + "FROM  "
          + "(SELECT SUM(price) AS summa,users.id "
          + "FROM ordered_certificates "
          + "JOIN orders ON order_id=orders.id JOIN users ON user_id=users.id "
          + "GROUP BY users.id) AS user_orders_cost "
          + "ORDER BY summa DESC LIMIT 1) ";

  private static final String SQL_REQUEST_FOR_WIDELY_USED_TAG_FROM_HIGHEST_COST_ORDERS_USER =
      "SELECT ordered_tags.id, ordered_tags.name "
          + "FROM ordered_tags "
          + "JOIN ordered_certificates_tags ON ordered_tags.id=tag_id "
          + "JOIN ordered_certificates ON ordered_certificates.id=certificate_id "
          + "JOIN orders ON orders.id=order_id JOIN users ON users.id=user_id "
          + "WHERE users.id="
          + SQL_REQUEST_FOR_USER_ID_WITH_HIGHEST_COST_ORDERS
          + "GROUP BY ordered_tags.name "
          + "ORDER BY COUNT(ordered_tags.name) DESC LIMIT 1;";

  private final PaginationHandler paginationHandler;
  private final EntityManager entityManager;

  public UserDaoImpl(PaginationHandler paginationHandler, EntityManager entityManager) {
    this.paginationHandler = paginationHandler;
    this.entityManager = entityManager;
  }

  @Override
  public User create(User user) {
    entityManager.persist(user);
    return user;
  }

  @Override
  public User create(UserRepresentation userRepresentation) {
    String name = userRepresentation.getFirstName();
    String surname = userRepresentation.getLastName();
    String foreignId = userRepresentation.getUsername();
    User user = User.builder().name(name).surname(surname).foreignId(foreignId).build();
    entityManager.persist(user);
    return user;
  }

  @Override
  public Optional<User> read(long id) {
    return Optional.ofNullable(entityManager.find(User.class, id));
  }

  @Override
  public Optional<User> readByForeignId(String foreignId) {
    String hql = "from  User where foreignId=:foreignId";
    Query query = entityManager.createQuery(hql).setParameter("foreignId", foreignId);
    return query.getResultStream().findFirst();
  }

  @Override
  public PageData<User> readAll(PaginationParameter parameter) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
    Root<User> from = criteriaQuery.from(User.class);
    CriteriaQuery<User> select = criteriaQuery.select(from);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
    countQuery.select(builder.count(countQuery.from(User.class)));
    Long numberOfElements = entityManager.createQuery(countQuery).getSingleResult();
    long numberOfPages =
        paginationHandler.calculateNumberOfPages(numberOfElements, parameter.getSize());

    TypedQuery<User> typedQuery = entityManager.createQuery(select);
    paginationHandler.setPageToQuery(typedQuery, parameter);
    List<User> users = typedQuery.getResultList();

    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, users);
  }

  @Override
  public Tag takeMostWidelyTagFromUserWithHighestCostOrders() {
    Query query =
        entityManager.createNativeQuery(
            SQL_REQUEST_FOR_WIDELY_USED_TAG_FROM_HIGHEST_COST_ORDERS_USER);

    Optional<Object[]> tagValue = query.getResultStream().findFirst();
    if (tagValue.isPresent()) {
      long id = ((BigInteger) tagValue.get()[0]).longValue();
      String name = (String) tagValue.get()[1];
      return Tag.builder().id(id).name(name).build();
    }
    throw new TagException("There is no any tags in orders");
  }
}
