package com.epam.esm.web.rest;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class OrderResourceSecurityTest {
  @Autowired MockMvc mockMvc;
  @Autowired UserDao userDao;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;
  @Autowired OrderService orderService;

  @AfterEach
  void setDown() {
    String sql =
        "DELETE FROM ordered_certificates_tags;DELETE FROM ordered_tags;DELETE FROM ordered_certificates;"
            + "DELETE FROM orders;DELETE FROM users;";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  @Disabled("method uses keycloak")
  @WithMockUser(roles = "USER")
  void readUserOrderAuth() throws Exception {
    Order order = givenOrder();
    User user = givenUser();
    Long userId = userDao.create(user).getId();
    user.setId(userId);
    order.setUser(user);
    Long orderId =
        orderService.create(new OrderWithCertificatesWithTagsForCreationDto(order)).getId();

    mockMvc
        .perform(get("/users/{userId}/order/{orderId}", userId, orderId))
        .andExpect(status().isOk());
  }

  @Test
  @Disabled("method uses keycloak")
  void readUserOrderNoAuth() throws Exception {
    Order order = givenOrder();
    User user = givenUser();
    Long userId = userDao.create(user).getId();
    user.setId(userId);
    order.setUser(user);
    Long orderId =
        orderService.create(new OrderWithCertificatesWithTagsForCreationDto(order)).getId();

    mockMvc
        .perform(get("/users/{userId}/order/{orderId}", userId, orderId))
        .andExpect(status().isUnauthorized());
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
    user.setForeignId("someId");
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
