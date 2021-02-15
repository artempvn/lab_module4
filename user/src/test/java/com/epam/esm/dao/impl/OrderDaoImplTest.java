package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@SpringBootTest
class OrderDaoImplTest {
  public static final int NOT_EXISTING_USER_ID = 9999999;
  public static final int NOT_EXISTING_ORDER_ID = 9999999;
  @Autowired OrderDao orderDao;
  @Autowired UserDao userDao;
  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;

  @AfterEach
  void setDown() {
    String sql =
        "DELETE FROM ordered_certificates_tags;DELETE FROM ordered_tags;DELETE FROM ordered_certificates;"
            + "DELETE FROM orders;DELETE FROM users;";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  void create() {
    User user = givenUser();
    long id = userDao.create(user).getId();
    Tag tag = givenTag();
    var tag1 = tagDao.create(tag);
    Certificate certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    Order order = givenOrder();
    user.setId(id);
    order.setUser(user);
    order.setCertificates(List.of(certificate1));

    Order actualOrder = orderDao.create(order);

    assertNotNull(actualOrder.getId());
  }

  @Test
  void readAllByUser() {
    User user = givenUser();
    long id = userDao.create(user).getId();
    Tag tag = givenTag();
    var tag1 = tagDao.create(tag);
    Certificate certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    Order order = givenOrder();
    user.setId(id);
    order.setUser(user);
    order.setCertificates(List.of(certificate1));
    orderDao.create(order);
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    parameter.setSize(10);

    PageData<Order> actualPage = orderDao.readAllByUser(id, parameter);

    assertEquals(1, actualPage.getContent().size());
  }

  @Test
  void readAllByNotExistingUser() {

    assertThrows(
        ResourceValidationException.class,
        () -> orderDao.readAllByUser(NOT_EXISTING_USER_ID, new PaginationParameter()));
  }

  @Test
  void readExistingOrderByUser() {
    User user = givenUser();
    long userId = userDao.create(user).getId();
    Tag tag = givenTag();
    var tag1 = tagDao.create(tag);
    Certificate certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    Order order = givenOrder();
    user.setId(userId);
    order.setUser(user);
    order.setCertificates(List.of(certificate1));
    long orderId = orderDao.create(order).getId();

    Optional<Order> actualOrder = orderDao.readOrderByUser(userId, orderId);

    assertTrue(actualOrder.isPresent());
  }

  @Test
  void readNonExistingOrderByUser() {
    User user = givenUser();
    long userId = userDao.create(user).getId();
    Tag tag = givenTag();
    var tag1 = tagDao.create(tag);
    Certificate certificate = givenCertificate();
    certificate.setTags(List.of(tag1));
    var certificate1 = certificateDao.create(certificate);
    Order order = givenOrder();
    user.setId(userId);
    order.setUser(user);
    order.setCertificates(List.of(certificate1));

    Optional<Order> actualOrder = orderDao.readOrderByUser(userId, NOT_EXISTING_ORDER_ID);

    assertTrue(actualOrder.isEmpty());
  }

  Order givenOrder() {
    Order order = new Order();
    var certificate = givenCertificate();
    order.setCertificates(List.of(certificate));
    return order;
  }

  User givenUser() {
    User user = new User();
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
