package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserDtoWithOrders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class UserDaoImplTest {
  public static final int NOT_EXISTING_USER_ID = 9999999;
  @Autowired UserDao userDao;
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
    UserDto user = givenUser1WO();

    UserDto actualUser = userDao.create(user);

    assertNotNull(actualUser.getId());
  }

  @Test
  void readExisted() {
    UserDto user = givenUser1WO();
    long id = userDao.create(user).getId();

    Optional<UserDtoWithOrders> actualUser = userDao.read(id);

    assertTrue(actualUser.isPresent());
  }

  @Test
  void readNotExisted() {
    Optional<UserDtoWithOrders> actualUser = userDao.read(NOT_EXISTING_USER_ID);

    assertFalse(actualUser.isPresent());
  }

  @Test
  void readAll() {
    UserDto user1 = givenUser1WO();
    UserDto user2 = givenUser2WO();
    userDao.create(user1);
    userDao.create(user2);
    List<UserDto> expectedList = List.of(user1, user2);
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    parameter.setSize(10);

    PageData<UserDto> actualPage = userDao.readAll(parameter);

    assertEquals(expectedList.size(), actualPage.getContent().size());
  }

  UserDto givenUser1WO() {
    UserDto user = new UserDto();
    user.setName("name");
    user.setSurname("surname");
    return user;
  }

  UserDto givenUser2WO() {
    UserDto user = new UserDto();
    user.setName("name1");
    user.setSurname("surname1");
    return user;
  }
}
