package com.epam.esm.dao.impl;

import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserDtoWithOrders;
import com.epam.esm.exception.TagException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
      "(SELECT id FROM  (SELECT SUM(price) AS summa,users.id FROM ordered_certificates "
          + "JOIN orders ON order_id=orders.id JOIN users ON user_id=users.id GROUP BY users.id) AS user_orders_cost"
          + " ORDER BY summa desc limit 1)";

  private static final String SQL_REQUEST_FOR_WIDELY_USED_TAG_FROM_HIGHEST_COST_ORDERS_USER =
      "SELECT ordered_tags.id, ordered_tags.name FROM ordered_tags JOIN ordered_certificates_tags "
          + "ON ordered_tags.id=tag_id JOIN ordered_certificates ON ordered_certificates.id=certificate_id "
          + "JOIN orders ON orders.id=order_id JOIN users ON users.id=user_id WHERE users.id="
          + SQL_REQUEST_FOR_USER_ID_WITH_HIGHEST_COST_ORDERS
          + " GROUP BY ordered_tags.name ORDER BY count(ordered_tags.name) desc limit 1;";
  private final SessionFactory sessionFactory;
  private final PaginationHandler paginationHandler;

  public UserDaoImpl(SessionFactory sessionFactory, PaginationHandler paginationHandler) {
    this.sessionFactory = sessionFactory;
    this.paginationHandler = paginationHandler;
  }

  @Override
  public UserDto create(UserDto dto) {
    User user = new User(dto);
    Session session = sessionFactory.getCurrentSession();
    session.save(user);
    return new UserDto(user);
  }

  @Override
  public Optional<UserDtoWithOrders> read(long id) {
    Session session = sessionFactory.getCurrentSession();
    Optional<User> user = Optional.ofNullable(session.get(User.class, id));
    return user.map(UserDtoWithOrders::new);
  }

  @Override
  public Optional<UserDto> readWithoutOrders(long id) {
    Session session = sessionFactory.getCurrentSession();
    Optional<User> user = Optional.ofNullable(session.get(User.class, id));
    return user.map(UserDto::new);
  }

  @Override
  public List<UserDto> readAll(PaginationParameter parameter) {
    Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
    Root<User> from = criteriaQuery.from(User.class);
    CriteriaQuery<User> select = criteriaQuery.select(from);

    TypedQuery<User> typedQuery = session.createQuery(select);
    paginationHandler.setPageToQuery(typedQuery, parameter);

    List<User> users = typedQuery.getResultList();
    return users.stream().map(UserDto::new).collect(Collectors.toList());
  }

  @Override
  public TagDto takeMostWidelyTagFromUserWithHighestCostOrders() {
    Session session = sessionFactory.getCurrentSession();
    Query q =
        session.createNativeQuery(SQL_REQUEST_FOR_WIDELY_USED_TAG_FROM_HIGHEST_COST_ORDERS_USER);

    Optional<Object[]> tagValue = q.getResultStream().findFirst();
    if (tagValue.isPresent()) {
      long id = ((BigInteger) tagValue.get()[0]).longValue();
      String name = (String) tagValue.get()[1];
      return TagDto.builder().id(id).name(name).build();
    }
    throw new TagException("There is no any tags in orders");
  }
}
