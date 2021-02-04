package com.epam.esm.dao.impl;

import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dto.*;
import com.epam.esm.exception.TagException;
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
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserDaoImpl implements UserDao {

  private static final String SQL_REQUEST_FOR_USER_ID_WITH_HIGHEST_COST_ORDERS =
      "(SELECT id "
          + "FROM  "
          + "(SELECT SUM(price) AS summa,users.id "
          + "FROM ordered_certificates "
          + "JOIN orders ON order_id=orders.id JOIN users ON user_id=users.id "
          + "GROUP BY users.id) AS user_orders_cost"
          + " ORDER BY summa desc limit 1)";

  private static final String SQL_REQUEST_FOR_WIDELY_USED_TAG_FROM_HIGHEST_COST_ORDERS_USER =
      "SELECT ordered_tags.id, ordered_tags.name "
          + "FROM ordered_tags "
          + "JOIN ordered_certificates_tags ON ordered_tags.id=tag_id "
          + "JOIN ordered_certificates ON ordered_certificates.id=certificate_id "
          + "JOIN orders ON orders.id=order_id JOIN users ON users.id=user_id "
          + "WHERE users.id="
          + SQL_REQUEST_FOR_USER_ID_WITH_HIGHEST_COST_ORDERS
          + " GROUP BY ordered_tags.name "
          + "ORDER BY count(ordered_tags.name) desc limit 1;";

  private final PaginationHandler paginationHandler;
  private final EntityManager entityManager;

  public UserDaoImpl(PaginationHandler paginationHandler, EntityManager entityManager) {
    this.paginationHandler = paginationHandler;
    this.entityManager = entityManager;
  }

  @Override
  public UserDto create(UserDto dto) {
    User user = new User(dto);
    entityManager.persist(user);
    return new UserDto(user);
  }

  @Override
  public Optional<UserDtoWithOrders> read(long id) {
    Optional<User> user = Optional.ofNullable(entityManager.find(User.class, id));
    return user.map(UserDtoWithOrders::new);
  }

  @Override
  public Optional<UserDto> readWithoutOrders(long id) {
    Optional<User> user = Optional.ofNullable(entityManager.find(User.class, id));
    return user.map(UserDto::new);
  }

  @Override
  public PageData<UserDto> readAll(PaginationParameter parameter) {
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
    List<UserDto> users =
        typedQuery.getResultList().stream().map(UserDto::new).collect(Collectors.toList());

    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, users);
  }

  @Override
  public TagDto takeMostWidelyTagFromUserWithHighestCostOrders() {
    Query q =
        entityManager.createNativeQuery(
            SQL_REQUEST_FOR_WIDELY_USED_TAG_FROM_HIGHEST_COST_ORDERS_USER);

    Optional<Object[]> tagValue = q.getResultStream().findFirst();
    if (tagValue.isPresent()) {
      long id = ((BigInteger) tagValue.get()[0]).longValue();
      String name = (String) tagValue.get()[1];
      return TagDto.builder().id(id).name(name).build();
    }
    throw new TagException("There is no any tags in orders");
  }
}
