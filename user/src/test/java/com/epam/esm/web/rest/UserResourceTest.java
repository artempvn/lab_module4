package com.epam.esm.web.rest;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.web.advice.ResourceAdvice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("user")
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
  void readUserPositive() throws Exception {
    UserDto user = givenUserWO1();
    long userId = userDao.create(user).getId();

    mockMvc
        .perform(get("/users/{id}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.name").value(user.getName()))
        .andExpect(jsonPath("$.surname").value(user.getSurname()));
  }

  @Test
  void readUserNegative() throws Exception {

    mockMvc.perform(get("/users/{id}", NOT_EXISTING_ID)).andExpect(status().isNotFound());
  }

  @Test
  void readUsers() throws Exception {
    UserDto user1 = givenUserWO1();
    UserDto user2 = givenUserWO2();
    long userId1 = userDao.create(user1).getId();
    long userId2 = userDao.create(user2).getId();
    UserDto user1WO = givenUserWO1();
    UserDto user2WO = givenUserWO2();
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

  UserDto givenUserWO1() {
    UserDto user = new UserDto();
    user.setName("name1");
    user.setSurname("surname1");
    return user;
  }

  UserDto givenUserWO2() {
    UserDto user = new UserDto();
    user.setName("name2");
    user.setSurname("surname2");
    return user;
  }
}
