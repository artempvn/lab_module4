package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateWithTagsDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderWithCertificatesDto;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.security.AuthorizeAccess;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
  private final UserDao userDao;
  private final OrderDao orderDao;
  private final CertificateService certificateService;

  public OrderServiceImpl(
      UserDao userDao, OrderDao orderDao, CertificateService certificateService) {
    this.userDao = userDao;
    this.orderDao = orderDao;
    this.certificateService = certificateService;
  }

  @Override
  public OrderWithCertificatesWithTagsForCreationDto create(
      OrderWithCertificatesWithTagsForCreationDto order) {
    userDao
        .read(order.getUserId())
        .orElseThrow(ResourceValidationException.validationWithUser(order.getUserId()));

    LocalDateTime timeNow = LocalDateTime.now();
    order.setCreateDate(timeNow);

    List<CertificateWithTagsDto> certificates =
        order.getCertificates().stream()
            .map(certificateService::create)
            .collect(Collectors.toList());
    order.setCertificates(certificates);

    Order orderEntity = new Order(order);
    return new OrderWithCertificatesWithTagsForCreationDto(orderDao.create(orderEntity));
  }

  @Override
  @AuthorizeAccess("userId")
  public PageData<OrderDto> readAllByUser(long userId, PaginationParameter parameter) {
    PageData<Order> pageData = orderDao.readAllByUser(userId, parameter);
    long numberOfElements = pageData.getNumberOfElements();
    long numberOfPages = pageData.getNumberOfPages();
    List<OrderDto> orders =
        pageData.getContent().stream().map(OrderDto::new).collect(Collectors.toList());
    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, orders);
  }

  @Override
  @AuthorizeAccess("userId")
  public OrderWithCertificatesDto readOrderByUser(long userId, long orderId) {
    Optional<User> user = userDao.read(userId);
    user.orElseThrow(ResourceValidationException.validationWithUser(userId));

    Optional<Order> order = orderDao.readOrderByUser(userId, orderId);
    return order
        .map(OrderWithCertificatesDto::new)
        .orElseThrow(ResourceNotFoundException.notFoundWithOrder(orderId));
  }
}
