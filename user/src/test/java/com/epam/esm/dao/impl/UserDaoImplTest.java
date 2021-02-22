package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateWithTagsDto;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@SpringBootTest
class UserDaoImplTest {
  public static final int NOT_EXISTING_USER_ID = 9999999;
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
  void create() {
    User user = givenUser1WO();

    User actualUser = userDao.create(user);

    assertNotNull(actualUser.getId());
  }

  @Test
  void readExisted() {
    User user = givenUser1WO();
    long id = userDao.create(user).getId();

    Optional<User> actualUser = userDao.read(id);

    assertTrue(actualUser.isPresent());
  }

  @Test
  void readNotExisted() {
    Optional<User> actualUser = userDao.read(NOT_EXISTING_USER_ID);

    assertFalse(actualUser.isPresent());
  }

  @Test
  void readAll() {
    User user1 = givenUser1WO();
    User user2 = givenUser2WO();
    userDao.create(user1);
    userDao.create(user2);
    List<User> expectedList = List.of(user1, user2);
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    parameter.setSize(10);

    PageData<User> actualPage = userDao.readAll(parameter);

    assertEquals(expectedList.size(), actualPage.getContent().size());
  }

  @Test
  @Disabled("method uses keycloak")
  void takeMostWidelyTagFromUserWithHighestCostOrders() {
    User userWithHighestCostOfOrders = givenUser1WO();
    User user = givenUser2WO();
    long userHighestCostId = userDao.create(userWithHighestCostOfOrders).getId();
    long userId = userDao.create(user).getId();
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
    OrderWithCertificatesWithTagsForCreationDto order3 =
        OrderWithCertificatesWithTagsForCreationDto.builder()
            .userId(userId)
            .certificates(List.of(certificate2))
            .build();
    orderService.create(order1);
    orderService.create(order2);
    orderService.create(order3);
    String expectedTagName = tag1.getName();

    String actualTagName = userDao.takeMostWidelyTagFromUserWithHighestCostOrders().getName();

    assertEquals(expectedTagName, actualTagName);
  }

  User givenUser1WO() {
    User user = new User();
    user.setName("name");
    user.setSurname("surname");
    return user;
  }

  User givenUser2WO() {
    User user = new User();
    user.setName("name1");
    user.setSurname("surname1");
    return user;
  }
}
