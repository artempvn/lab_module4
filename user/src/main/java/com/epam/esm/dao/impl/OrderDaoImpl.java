package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoFull;
import com.epam.esm.dto.UserDtoWithOrders;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class OrderDaoImpl implements OrderDao {

  private final SessionFactory sessionFactory;

  public OrderDaoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public OrderDtoFull create(OrderDtoFull dto) {
    Order order = new Order(dto);
    Session session = sessionFactory.getCurrentSession();
    session.save(order);
    return new OrderDtoFull(order);
  }

  @Override
  public Optional<OrderDtoFull> read(long id) {
    return Optional.empty();
  }

  @Override
  public List<OrderDto> readAll() {
    return null;
  }
}
