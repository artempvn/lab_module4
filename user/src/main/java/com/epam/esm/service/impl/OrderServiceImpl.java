package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
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
  public OrderDtoFullCreation create(OrderDtoFullCreation order) {
    userDao
        .read(order.getUserId())
        .orElseThrow(ResourceValidationException.validationWithOrder(order.getUserId()));

    LocalDateTime timeNow = LocalDateTime.now();
    order.setCreateDate(timeNow);

    List<CertificateDtoFull> certificates =
        order.getCertificates().stream()
            .map(certificateService::create)
            .collect(Collectors.toList());
    order.setCertificates(certificates);

    return orderDao.create(order);
  }

  @Override
  public List<OrderDto> readAllByUser(long userId) {
    return orderDao.readAllByUser(userId);
  }

  @Override
  public OrderDtoFull readOrderByUser(long userId, long orderId) {
    Optional<OrderDtoFull> order = orderDao.readOrderByUser(userId, orderId);
    OrderDtoFull existingOrder =
        order.orElseThrow(ResourceNotFoundException.notFoundWithOrder(orderId));
    double sum =
        existingOrder.getCertificates().stream().mapToDouble(CertificateDto::getPrice).sum();
    existingOrder.setPrice(sum);
    return existingOrder;
  }
}
