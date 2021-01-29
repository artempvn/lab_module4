package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDtoFull;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

  private static final long USER_ID = 1L;
  UserDao userDao = mock(UserDao.class);

  UserService userService = new UserServiceImpl(userDao);

  @Test
  void read() {
    UserDtoFull user = givenUser();
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
    userService.readAll();

    verify(userDao).readAll();
  }

  UserDtoFull givenUser() {
    UserDtoFull user = new UserDtoFull();
    user.setName("name");
    user.setSurname("surname");
    return user;
  }
}
