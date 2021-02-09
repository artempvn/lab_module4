package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

  private static final long USER_ID = 1L;
  UserDao userDao = mock(UserDao.class);

  UserService userService = new UserServiceImpl(userDao);

  @Test
  void read() {
    User user = givenUser();
    when(userDao.read(anyLong())).thenReturn(Optional.of(user));

    userService.read(USER_ID);

    verify(userDao).read(anyLong());
  }

  @Test
  void readException() {
    when(userDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> userService.read(USER_ID));
  }

  @Test
  void readAll() {
    PageData<User> pageData = new PageData<>();
    pageData.setNumberOfElements(1);
    pageData.setNumberOfPages(1);
    pageData.setContent(Collections.emptyList());
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    when(userDao.readAll(any())).thenReturn(pageData);

    userService.readAll(parameter);

    verify(userDao).readAll(any());
  }

  @Test
  void takeMostWidelyTagFromUserWithHighestCostOrders() {
    Tag tag = Tag.builder().id(1L).build();
    when(userDao.takeMostWidelyTagFromUserWithHighestCostOrders()).thenReturn(tag);

    userService.takeMostWidelyTagFromUserWithHighestCostOrders();

    verify(userDao).takeMostWidelyTagFromUserWithHighestCostOrders();
  }

  User givenUser() {
    User user = new User();
    user.setName("name");
    user.setSurname("surname");
    return user;
  }
}
