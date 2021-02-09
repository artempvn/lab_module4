package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateWithTagsDto;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
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
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

  public static final long USER_ID = 1L;
  private static final long ORDER_ID = 1L;
  UserDao userDao = mock(UserDao.class);
  OrderDao orderDao = mock(OrderDao.class);
  CertificateService certificateService = mock(CertificateService.class);

  OrderService orderService = new OrderServiceImpl(userDao, orderDao, certificateService);

  @Test
  void createOrderDaoInvocation() {
    Order order = givenOrder();
    order.setId(1L);
    User user = givenUser();
    when(userDao.readWithoutOrders(anyLong())).thenReturn(Optional.of(user));
    CertificateWithTagsDto certificate = CertificateWithTagsDto.builder().id(1L).build();
    OrderWithCertificatesWithTagsForCreationDto inputOrder =
        OrderWithCertificatesWithTagsForCreationDto.builder()
            .userId(1L)
            .certificates(List.of(certificate))
            .build();
    when(certificateService.create(any())).thenReturn(certificate);
    when(orderDao.create(any())).thenReturn(order);

    orderService.create(inputOrder);

    verify(orderDao).create(any());
  }

  @Test
  void createCertificateServiceInvocation() {
    Order order = givenOrder();
    order.setId(1L);
    User user = givenUser();
    when(userDao.readWithoutOrders(anyLong())).thenReturn(Optional.of(user));
    CertificateWithTagsDto certificate = CertificateWithTagsDto.builder().id(1L).build();
    OrderWithCertificatesWithTagsForCreationDto inputOrder =
        OrderWithCertificatesWithTagsForCreationDto.builder()
            .userId(1L)
            .certificates(List.of(certificate))
            .build();
    when(certificateService.create(any())).thenReturn(certificate);
    when(orderDao.create(any())).thenReturn(order);

    orderService.create(inputOrder);

    verify(certificateService).create(any());
  }

  @Test
  void createException() {
    OrderWithCertificatesWithTagsForCreationDto order =
        OrderWithCertificatesWithTagsForCreationDto.builder().userId(1L).build();
    when(userDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceValidationException.class, () -> orderService.create(order));
  }

  @Test
  void readAllByUserOrderDaoInvocation() {
    long userId = 1L;
    PageData<Order> pageData = new PageData<>();
    pageData.setNumberOfElements(1);
    pageData.setNumberOfPages(1);
    pageData.setContent(Collections.emptyList());
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    when(orderDao.readAllByUser(anyLong(), any())).thenReturn(pageData);

    orderService.readAllByUser(userId, parameter);

    verify(orderDao).readAllByUser(anyLong(), any());
  }

  @Test
  void readOrderByUserOrderDaoInvocation() {
    User user = givenUser();
    when(userDao.read(anyLong())).thenReturn(Optional.of(user));
    Order order = givenOrder2();
    when(orderDao.readOrderByUser(anyLong(), anyLong())).thenReturn(Optional.of(order));

    orderService.readOrderByUser(USER_ID, ORDER_ID);

    verify(orderDao).readOrderByUser(USER_ID, ORDER_ID);
  }

  @Test
  void readOrderByUserException() {
    User user = givenUser();
    when(userDao.read(anyLong())).thenReturn(Optional.of(user));
    when(orderDao.readOrderByUser(anyLong(), anyLong())).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> orderService.readOrderByUser(USER_ID, ORDER_ID));
  }

  @Test
  void readOrderByNotExistingUser() {
    when(userDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(
        ResourceValidationException.class, () -> orderService.readOrderByUser(USER_ID, ORDER_ID));
  }

  Order givenOrder() {
    Order order = new Order();
    var certificate = givenCertificate();
    order.setCertificates(List.of(certificate));
    User user = givenUser();
    order.setUser(user);
    return order;
  }

  Order givenOrder2() {
    return Order.builder().certificates(Collections.emptyList()).build();
  }

  User givenUser() {
    User user = new User();
    user.setId(1L);
    user.setName("name");
    user.setSurname("surname");
    return user;
  }

  Certificate givenCertificate() {
    Certificate certificate = new Certificate();
    certificate.setPreviousId(99L);
    certificate.setPrice(99.99);
    var tag = givenTag();
    certificate.setTags(List.of(tag));
    return certificate;
  }

  Tag givenTag() {
    Tag tag = new Tag();
    tag.setName("tag name");
    return tag;
  }
}
