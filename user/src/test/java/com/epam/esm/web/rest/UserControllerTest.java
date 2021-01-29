package com.epam.esm.web.rest;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDtoFull;
import com.epam.esm.dto.UserDto;
import com.epam.esm.web.advice.ResourceAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class UserControllerTest {

  public static final int NOT_EXISTING_ID = 99999;
  MockMvc mockMvc;
  @Autowired UserDao userDao;
  @Autowired
  UserController userController;
  @Autowired SessionFactory sessionFactory;
  @Autowired ReloadableResourceBundleMessageSource messageSource;

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
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      String sql = "DELETE FROM users";
      session.createNativeQuery(sql).executeUpdate();
      session.getTransaction().commit();
    }
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
        .perform(get("/users"))
        .andExpect(
            content().json(new ObjectMapper().writeValueAsString(List.of(user1WO, user2WO))));
  }

  UserDtoFull givenUser1() {
    UserDtoFull user = new UserDtoFull();
    user.setName("name1");
    user.setSurname("surname1");
    return user;
  }

  UserDtoFull givenUser2() {
    UserDtoFull user = new UserDtoFull();
    user.setName("name2");
    user.setSurname("surname2");
    return user;
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
