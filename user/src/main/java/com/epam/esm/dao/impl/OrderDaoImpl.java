package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoWithCertificates;
import com.epam.esm.dto.OrderDtoWithCertificatesWithTagsForCreation;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.exception.ResourceValidationException;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class OrderDaoImpl implements OrderDao {

  private final SessionFactory sessionFactory;
  private final PaginationHandler paginationHandler;

  public OrderDaoImpl(SessionFactory sessionFactory, PaginationHandler paginationHandler) {
    this.sessionFactory = sessionFactory;
    this.paginationHandler = paginationHandler;
  }

  @Override
  public OrderDtoWithCertificatesWithTagsForCreation create(
      OrderDtoWithCertificatesWithTagsForCreation dto) {
    Order order = new Order(dto);
    Session session = sessionFactory.getCurrentSession();
    session.save(order);

    order.getCertificates().stream()
        .map(certificate -> session.load(Certificate.class, certificate.getId()))
        .forEach(certificate -> certificate.setOrder(order));

    return new OrderDtoWithCertificatesWithTagsForCreation(order);
  }

  @Override
  public List<OrderDto> readAllByUser(long userId, PaginationParameter parameter) {
    Session session = sessionFactory.getCurrentSession();
    Optional.ofNullable(session.get(User.class, userId))
        .orElseThrow(ResourceValidationException.validationWithUser(userId));

    Query<Order> query = session.createQuery("From Order where user.id=:user");
    query.setParameter("user", userId);
    paginationHandler.setPageToQuery(query, parameter);
    List<Order> orders = query.list();

    return orders.stream().map(OrderDto::new).collect(Collectors.toList());
  }

  @Override
  public Optional<OrderDtoWithCertificates> readOrderByUser(long userId, long orderId) {
    Session session = sessionFactory.getCurrentSession();
    Optional.ofNullable(session.get(User.class, userId))
        .orElseThrow(ResourceValidationException.validationWithUser(userId));

    Optional<Order> order = Optional.ofNullable(session.get(Order.class, orderId));
    return order.map(OrderDtoWithCertificates::new);
  }
}
