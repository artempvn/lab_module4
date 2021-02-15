package com.epam.esm.web.rest;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class UserResourceSecurityTest {
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
  @WithMockUser(roles = "USER")
  void readUserAuth() throws Exception {
    User user = givenUserWO1();
    long userId = userDao.create(user).getId();

    mockMvc.perform(get("/users/{id}", userId)).andExpect(status().isOk());
  }

  @Test
  void readUserNoAuth() throws Exception {
    User user = givenUserWO1();
    long userId = userDao.create(user).getId();

    mockMvc.perform(get("/users/{id}", userId)).andExpect(status().isForbidden());
  }

  User givenUserWO1() {
    User user = new User();
    user.setName("name1");
    user.setSurname("surname1");
    return user;
  }
}
