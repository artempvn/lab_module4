package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoFull;
import com.epam.esm.dto.OrderDtoFullCreation;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class OrderDaoImpl implements OrderDao {

  private final SessionFactory sessionFactory;

  public OrderDaoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public OrderDtoFullCreation create(OrderDtoFullCreation dto) {
    Order order = new Order(dto);
    Session session = sessionFactory.getCurrentSession();
    session.save(order);

    order.getCertificates().stream()
        .map(certificate -> session.load(Certificate.class, certificate.getId()))
        .forEach(certificate -> certificate.setOrder(order));

    return new OrderDtoFullCreation(order);
  }

  @Override
  public List<OrderDto> readAllByUser(long userId) {
    Session session = sessionFactory.getCurrentSession();
    Optional<User> user = Optional.ofNullable(session.get(User.class, userId));

    List<Order> orders =
        user.orElseThrow(ResourceValidationException.validationWithUser(userId)).getOrders();

    return orders.stream().map(OrderDto::new).collect(Collectors.toList());
  }

  @Override
  public Optional<OrderDtoFull> readOrderByUser(long userId, long orderId) {
    Session session = sessionFactory.getCurrentSession();
    Optional.ofNullable(session.get(User.class, userId))
        .orElseThrow(ResourceValidationException.validationWithUser(userId));

    Optional<Order> order = Optional.ofNullable(session.get(Order.class, orderId));
    return order.map(OrderDtoFull::new);
  }
}
