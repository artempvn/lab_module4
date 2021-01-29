package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class OrderDaoImplTest {
  public static final int NOT_EXISTING_USER_ID = 9999999;
  @Autowired OrderDao orderDao;

  @Autowired UserDao userDao;
  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;

  @Autowired SessionFactory sessionFactory;

  @AfterEach
  void setDown() {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      String sql =
          "DELETE FROM certificates_tags_backup;DELETE FROM tags_backup;DELETE FROM certificates_backup;"
              + "DELETE FROM orders;DELETE FROM users;";
      session.createNativeQuery(sql).executeUpdate();
      session.getTransaction().commit();
    }
  }

  @Test
  void create() {
    UserDto user = givenUser();
    long id = userDao.create(user).getId();
    TagDto tag = givenTag();
    var tag1 = tagDao.create(tag);
    CertificateDtoFull certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    OrderDtoFull order = givenOrder();
    order.setCertificates(List.of(certificate1));

    OrderDtoFull actualOrder = orderDao.create(order);

    assertNotNull(actualOrder.getId());
  }

  @Test
  void read() {}

  @Test
  void readAll() {}

  OrderDtoFull givenOrder() {
    OrderDtoFull order = new OrderDtoFull();
    var certificate = givenCertificate();
    order.setCertificates(List.of(certificate));
    var user = givenUser();
    order.setUser(user);
    return order;
  }

  UserDto givenUser() {
    UserDto user = new UserDto();
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
