package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDtoFull;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public UserDtoFull read(long id) {
    Optional<UserDtoFull> user = userDao.read(id);
    return user.orElseThrow(ResourceNotFoundException.notFoundWithUser(id));
  }

  @Override
  public List<UserDto> readAll() {
    return userDao.readAll();
  }
}
