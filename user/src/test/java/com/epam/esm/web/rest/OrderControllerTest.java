package com.epam.esm.web.rest;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.OrderDtoWithCertificatesWithTagsForCreation;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.advice.ResourceAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
class OrderControllerTest {

  MockMvc mockMvc;
  @Autowired UserDao userDao;
  @Autowired OrderDao orderDao;
  @Autowired OrderService orderService;
  @Autowired OrderController orderController;
  @Autowired EntityManager entityManager;
  @Autowired ReloadableResourceBundleMessageSource messageSource;

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
    Session session = entityManager.unwrap(Session.class);
    String sql =
        "DELETE FROM ordered_certificates_tags;DELETE FROM ordered_tags;DELETE FROM ordered_certificates;"
            + "DELETE FROM orders;DELETE FROM users;";
    session.createNativeQuery(sql).executeUpdate();
  }

  @Test
  void createOrderValueCheck() throws Exception {
    OrderDtoWithCertificatesWithTagsForCreation order = givenOrder();
    UserDto user = givenUser();
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
  void readUserOrder() throws Exception {
    OrderDtoWithCertificatesWithTagsForCreation order = givenOrder();
    UserDto user = givenUser();
    Long userId = userDao.create(user).getId();
    order.setUserId(userId);
    Long orderId = orderService.create(order).getId();

    mockMvc
        .perform(get("/users/{userId}/order/{orderId}", userId, orderId))
        .andExpect(jsonPath("$.id").value(orderId))
        .andExpect(status().isOk());
  }

  @Test
  void readUserOrders() throws Exception {
    OrderDtoWithCertificatesWithTagsForCreation order = givenOrder();
    UserDto user = givenUser();
    Long userId = userDao.create(user).getId();
    order.setUserId(userId);
    orderService.create(order);

    mockMvc.perform(get("/users/{userId}/orders", userId)).andExpect(status().isOk());
  }

  OrderDtoWithCertificatesWithTagsForCreation givenOrder() {
    OrderDtoWithCertificatesWithTagsForCreation order =
        new OrderDtoWithCertificatesWithTagsForCreation();
    var certificate = givenCertificate();
    order.setCertificates(List.of(certificate));
    return order;
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
