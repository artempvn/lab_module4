package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDtoFull;
import com.epam.esm.dto.UserDtoWithOrders;
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
  private final TagDao tagDao;
  private final CertificateDao certificateDao;
  private final UserDao userDao;
  private final OrderDao orderDao;
  private final TagService tagService;
  private final CertificateService certificateService;

  public OrderServiceImpl(
      TagDao tagDao,
      CertificateDao certificateDao,
      UserDao userDao,
      OrderDao orderDao,
      TagService tagService,
      CertificateService certificateService) {
    this.tagDao = tagDao;
    this.certificateDao = certificateDao;
    this.userDao = userDao;
    this.orderDao = orderDao;
    this.tagService = tagService;
    this.certificateService = certificateService;
  }

  @Override
  public OrderDtoFull create(OrderDtoFull order) {
    long userId = order.getUser().getId();
    Optional<UserDtoWithOrders> user = userDao.read(userId);
    user.orElseThrow(ResourceValidationException.validationWithCertificateId(userId));

    LocalDateTime timeNow = LocalDateTime.now();
    order.setCreateDate(timeNow);

    OrderDtoFull orderDtoFull = orderDao.create(order);


    List<CertificateDto> certificates =
        order.getCertificates().stream()
                .peek(certificateDto -> certificateDto.setOrder(orderDtoFull))
            .map(certificateService::create)
            .collect(Collectors.toList());
    orderDtoFull.setCertificates(certificates);

    return orderDtoFull;
  }
}

//  OrderDtoFull createdOrder = orderDao.create(order);
//
//
//  List<CertificateDto> certificates =
//          order.getCertificates().stream()
//                  .peek(certificateDto -> certificateDto.setOrder(createdOrder))
//                  .map(certificateService::create)
//                  .collect(Collectors.toList());
//    createdOrder.setCertificates(certificates);
//
//            return createdOrder;


//  List<CertificateDto> certificates =
//          order.getCertificates().stream()
//                  .map(certificateService::create)
//                  .collect(Collectors.toList());
//    order.setCertificates(certificates);
//
//            return orderDao.create(order);