package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
  CertificateDao certificateDao = mock(CertificateDao.class);
  TagDao tagDao=mock(TagDao.class);
  UserDao userDao=mock(UserDao.class);
  OrderDao orderDao=mock(OrderDao.class);
  TagService tagService = mock(TagService.class);
  CertificateService certificateService=mock(CertificateService.class);

  OrderService orderService=new OrderServiceImpl(tagDao,certificateDao,userDao,orderDao,tagService,certificateService);

  @Test
  void createOrderDaoInvocation() {
    OrderDtoFull order=givenOrder();
    UserDtoWithOrders user=givenUser();
    when(userDao.read(anyLong())).thenReturn(Optional.of(user));

    orderService.create(order);

    verify(orderDao).create(order);
  }

  @Test
  void createCertificateServiceInvocation() {
    OrderDtoFull order=givenOrder();
    UserDtoWithOrders user=givenUser();
    when(userDao.read(anyLong())).thenReturn(Optional.of(user));

    orderService.create(order);

    verify(certificateService).create(any());
  }

  @Test
  void createException() {
    OrderDtoFull order=givenOrder();
    when(userDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceValidationException.class, ()->orderService.create(order));
  }

  OrderDtoFull givenOrder() {
    OrderDtoFull order=new OrderDtoFull();
    var certificate=givenCertificate();
    order.setCertificates(List.of(certificate));
    var user=givenUserWO();
    order.setUser(user);
    return order;
  }

  UserDtoWithOrders givenUser(){
    UserDtoWithOrders user=new UserDtoWithOrders();
    user.setId(1L);
    user.setName("name");
    user.setSurname("surname");
    return user;
  }

  UserDtoWithoutOrders givenUserWO(){
    UserDtoWithoutOrders user=new UserDtoWithoutOrders();
    user.setId(1L);
    user.setName("name");
    user.setSurname("surname");
    return user;
  }

  CertificateDto givenCertificate(){
    CertificateDto certificate=new CertificateDto();
    certificate.setPreviousId(99L);
    certificate.setPrice(99.99);
    var tag=givenTag();
    certificate.setTags(List.of(tag));
    return certificate;
  }

  TagDto givenTag(){
    TagDto tag=new TagDto();
    tag.setName("tag name");
    return tag;
  }
}