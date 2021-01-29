package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

  public static final long USER_ID = 1L;
  private static final long ORDER_ID = 1L;
  UserDao userDao = mock(UserDao.class);
  OrderDao orderDao = mock(OrderDao.class);
  CertificateService certificateService = mock(CertificateService.class);

  OrderService orderService = new OrderServiceImpl(userDao, orderDao, certificateService);

  @Test
  void createOrderDaoInvocation() {
    OrderDtoFullCreation order = givenOrder();
    UserDtoFull user = givenUser();
    when(userDao.read(anyLong())).thenReturn(Optional.of(user));

    orderService.create(order);

    verify(orderDao).create(order);
  }

  @Test
  void createCertificateServiceInvocation() {
    OrderDtoFullCreation order = givenOrder();
    UserDtoFull user = givenUser();
    when(userDao.read(anyLong())).thenReturn(Optional.of(user));

    orderService.create(order);

    verify(certificateService).create(any());
  }

  @Test
  void createException() {
    OrderDtoFullCreation order = givenOrder();
    when(userDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceValidationException.class, () -> orderService.create(order));
  }

  @Test
  void readAllByUserOrderDaoInvocation() {
    when(orderDao.readAllByUser(anyLong())).thenReturn(anyList());

    orderService.readAllByUser(USER_ID);

    verify(orderDao).readAllByUser(USER_ID);
  }

  @Test
  void readOrderByUserOrderDaoInvocation() {
    OrderDtoFull order = givenOrder2();
    when(orderDao.readOrderByUser(anyLong(), anyLong())).thenReturn(Optional.of(order));

    orderService.readOrderByUser(USER_ID, ORDER_ID);

    verify(orderDao).readOrderByUser(USER_ID, ORDER_ID);
  }

  @Test
  void readOrderByUserException() {
    when(orderDao.readOrderByUser(anyLong(), anyLong())).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> orderService.readOrderByUser(USER_ID, ORDER_ID));
  }

  OrderDtoFullCreation givenOrder() {
    OrderDtoFullCreation order = new OrderDtoFullCreation();
    var certificate = givenCertificate();
    order.setCertificates(List.of(certificate));
    order.setUserId(USER_ID);
    return order;
  }

  OrderDtoFull givenOrder2() {
    return OrderDtoFull.builder().certificates(Collections.emptyList()).build();
  }

  UserDtoFull givenUser() {
    UserDtoFull user = new UserDtoFull();
    user.setId(1L);
    user.setName("name");
    user.setSurname("surname");
    return user;
  }

  CertificateDtoFull givenCertificate() {
    CertificateDtoFull certificate = new CertificateDtoFull();
    certificate.setPreviousId(99L);
    certificate.setPrice(99.99);
    var tag = givenTag();
    certificate.setTags(List.of(tag));
    return certificate;
  }

  TagDto givenTag() {
    TagDto tag = new TagDto();
    tag.setName("tag name");
    return tag;
  }
}
