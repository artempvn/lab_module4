package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.exception.OrderException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
  public OrderDtoWithCertificatesWithTagsForCreation create(
      OrderDtoWithCertificatesWithTagsForCreation order) {
    userDao
        .readWithoutOrders(order.getUserId())
        .orElseThrow(ResourceValidationException.validationWithUser(order.getUserId()));

    LocalDateTime timeNow = LocalDateTime.now();
    order.setCreateDate(timeNow);

    if (order.getCertificates().isEmpty()) {
      throw OrderException.validationWithEmptyOrder(order.getUserId()).get();
    }

    List<CertificateDtoWithTags> certificates =
        order.getCertificates().stream()
            .map(certificateService::create)
            .collect(Collectors.toList());
    order.setCertificates(certificates);

    return orderDao.create(order);
  }

  @Override
  public PageData<OrderDto> readAllByUser(long userId, PaginationParameter parameter) {
    return orderDao.readAllByUser(userId, parameter);
  }

  @Override
  public OrderDtoWithCertificates readOrderByUser(long userId, long orderId) {
    Optional<OrderDtoWithCertificates> order = orderDao.readOrderByUser(userId, orderId);
    return order.orElseThrow(ResourceNotFoundException.notFoundWithOrder(orderId));
  }
}
