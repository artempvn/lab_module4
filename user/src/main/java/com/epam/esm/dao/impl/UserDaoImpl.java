package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDtoWithOrders;
import com.epam.esm.dto.UserDtoWithoutOrders;
import com.epam.esm.dao.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class UserDaoImpl implements UserDao {

  private final SessionFactory sessionFactory;

  public UserDaoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public UserDtoWithoutOrders create(UserDtoWithoutOrders dto) {
    User user = new User(dto);
    Session session = sessionFactory.getCurrentSession();
    session.save(user);
    return new UserDtoWithoutOrders(user);
  }

  @Override
  public Optional<UserDtoWithOrders> read(long id) {
    Session session = sessionFactory.getCurrentSession();
    Optional<User> user = Optional.ofNullable(session.get(User.class, id));
    return user.map(UserDtoWithOrders::new);
  }

  @Override
  public List<UserDtoWithoutOrders> readAll() {
    Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<User> criteria = builder.createQuery(User.class);
    criteria.from(User.class);
    List<User> tags = session.createQuery(criteria).list();
    return tags.stream().map(UserDtoWithoutOrders::new).collect(Collectors.toList());
  }
}
