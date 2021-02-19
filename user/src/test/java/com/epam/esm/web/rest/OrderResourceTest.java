package com.epam.esm.web.rest;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.advice.ResourceAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase
@SpringBootTest
class OrderResourceTest {

  MockMvc mockMvc;
  @Autowired UserDao userDao;
  @Autowired OrderDao orderDao;
  @Autowired OrderService orderService;
  @Autowired OrderResource orderController;
  @Autowired EntityManager entityManager;
  @Autowired ReloadableResourceBundleMessageSource messageSource;
  @Autowired TransactionTemplate txTemplate;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(orderController)
            .setControllerAdvice(new ResourceAdvice(messageSource))
            .build();
  }

  @AfterEach
  void setDown() {
    String sql =
        "DELETE FROM ordered_certificates_tags;DELETE FROM ordered_tags;DELETE FROM ordered_certificates;"
            + "DELETE FROM orders;DELETE FROM users;";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void createOrderValueCheck() throws Exception {
    Order order = givenOrder();
    User user = givenUser();
    Long userId = userDao.create(user).getId();

    mockMvc
        .perform(
            post("/users/{id}/order", userId)
                .content(new ObjectMapper().writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(status().isCreated());
  }

  @Test
  @Disabled("method uses keycloak")
  @WithMockUser(roles = "ADMIN")
  void readUserOrder() throws Exception {
    Order order = givenOrder();
    User user = givenUser();
    Long userId = userDao.create(user).getId();
    user.setId(userId);
    order.setUser(user);
    Long orderId =
        orderService.create(new OrderWithCertificatesWithTagsForCreationDto(order)).getId();

    mockMvc
        .perform(get("/users/{userId}/order/{orderId}", userId, orderId))
        .andExpect(jsonPath("$.id").value(orderId))
        .andExpect(status().isOk());
  }

  @Test
  @Disabled("method uses keycloak")
  @WithMockUser(roles = "ADMIN")
  void readUserOrders() throws Exception {
    Order order = givenOrder();
    User user = givenUser();
    Long userId = userDao.create(user).getId();
    user.setId(userId);
    order.setUser(user);
    orderService.create(new OrderWithCertificatesWithTagsForCreationDto(order));

    mockMvc
        .perform(get("/users/{userId}/orders?page=1&size=10", userId))
        .andExpect(status().isOk());
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
