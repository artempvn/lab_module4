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
    OrderDtoWithCertificatesWithTagsForCreation order = givenOrder();
    UserDto user = givenUser();
    when(userDao.readWithoutOrders(anyLong())).thenReturn(Optional.of(user));

    orderService.create(order);

    verify(orderDao).create(order);
  }

  @Test
  void createCertificateServiceInvocation() {
    OrderDtoWithCertificatesWithTagsForCreation order = givenOrder();
    UserDto user = givenUser();
    when(userDao.readWithoutOrders(anyLong())).thenReturn(Optional.of(user));

    orderService.create(order);

    verify(certificateService).create(any());
  }

  @Test
  void createException() {
    OrderDtoWithCertificatesWithTagsForCreation order = givenOrder();
    when(userDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceValidationException.class, () -> orderService.create(order));
  }

  @Test
  void readAllByUserOrderDaoInvocation() {
    when(orderDao.readAllByUser(anyLong(),any())).thenReturn(anyList());

    orderService.readAllByUser(anyLong(),null);

    verify(orderDao).readAllByUser(anyLong(),any());
  }

  @Test
  void readOrderByUserOrderDaoInvocation() {
    OrderDtoWithCertificates order = givenOrder2();
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

  OrderDtoWithCertificatesWithTagsForCreation givenOrder() {
    OrderDtoWithCertificatesWithTagsForCreation order = new OrderDtoWithCertificatesWithTagsForCreation();
    var certificate = givenCertificate();
    order.setCertificates(List.of(certificate));
    order.setUserId(USER_ID);
    return order;
  }

  OrderDtoWithCertificates givenOrder2() {
    return OrderDtoWithCertificates.builder().certificates(Collections.emptyList()).build();
  }

  UserDto givenUser() {
    UserDto user = new UserDto();
    user.setId(1L);
    user.setName("name");
    user.setSurname("surname");
    return user;
  }

  CertificateDtoWithTags givenCertificate() {
    CertificateDtoWithTags certificate = new CertificateDtoWithTags();
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
