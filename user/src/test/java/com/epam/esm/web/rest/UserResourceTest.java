package com.epam.esm.web.rest;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateWithTagsDto;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.dto.TagDto;
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

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase
@SpringBootTest
class UserResourceTest {

  public static final int NOT_EXISTING_ID = 99999;
  MockMvc mockMvc;
  @Autowired UserDao userDao;
  @Autowired UserResource userController;
  @Autowired EntityManager entityManager;
  @Autowired ReloadableResourceBundleMessageSource messageSource;
  @Autowired TransactionTemplate txTemplate;
  @Autowired OrderService orderService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(userController)
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
  @Disabled("method uses keycloak")
  @WithMockUser(roles = "ADMIN")
  void readUserPositive() throws Exception {
    User user = givenUserWO1();
    long userId = userDao.create(user).getId();

    mockMvc
        .perform(get("/users/{id}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.name").value(user.getName()))
        .andExpect(jsonPath("$.surname").value(user.getSurname()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void readUserNegative() throws Exception {

    mockMvc.perform(get("/users/{id}", NOT_EXISTING_ID)).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void readUsers() throws Exception {
    User user1 = givenUserWO1();
    User user2 = givenUserWO2();
    long userId1 = userDao.create(user1).getId();
    long userId2 = userDao.create(user2).getId();
    User user1WO = givenUserWO1();
    User user2WO = givenUserWO2();
    user1WO.setId(userId1);
    user2WO.setId(userId2);

    mockMvc
        .perform(get("/users?page=1&size=10"))
        .andExpect(jsonPath("$.currentPage").value(1))
        .andExpect(jsonPath("$.content").isNotEmpty())
        .andExpect(
            jsonPath(
                "$.links[?(@.rel=='self')].href",
                contains("http://localhost/users?page=1&size=10")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void readMostWidelyTagFromUserWithHighestCostOrders() throws Exception {
    User userWithHighestCostOfOrders = givenUserWO1();
    long userHighestCostId = userDao.create(userWithHighestCostOfOrders).getId();
    TagDto tag1 = TagDto.builder().name("tag1").build();
    TagDto tag2 = TagDto.builder().name("tag2").build();
    CertificateWithTagsDto certificate1 =
        CertificateWithTagsDto.builder().price(9999.).tags(List.of(tag1, tag2)).build();
    CertificateWithTagsDto certificate2 =
        CertificateWithTagsDto.builder().price(1.).tags(List.of(tag1)).build();
    OrderWithCertificatesWithTagsForCreationDto order1 =
        OrderWithCertificatesWithTagsForCreationDto.builder()
            .userId(userHighestCostId)
            .certificates(List.of(certificate1, certificate2))
            .build();
    OrderWithCertificatesWithTagsForCreationDto order2 =
        OrderWithCertificatesWithTagsForCreationDto.builder()
            .userId(userHighestCostId)
            .certificates(List.of(certificate2))
            .build();
    orderService.create(order1);
    orderService.create(order2);

    mockMvc
        .perform(get("/users/most-popular-tag"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("tag1"));
  }

  @Test
  @Disabled("method uses keycloak")
  void createUser() throws Exception {
    User user = givenUserWO1();

    mockMvc
        .perform(
            post("/users")
                .content(new ObjectMapper().writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
  }

  User givenUserWO1() {
    User user = new User();
    user.setName("name1");
    user.setSurname("surname1");
    return user;
  }

  User givenUserWO2() {
    User user = new User();
    user.setName("name2");
    user.setSurname("surname2");
    return user;
  }
}
