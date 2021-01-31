package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDtoWithOrders;
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
  public UserDtoWithOrders read(long id) {
    Optional<UserDtoWithOrders> user = userDao.read(id);
    return user.orElseThrow(ResourceNotFoundException.notFoundWithUser(id));
  }

  @Override
  public List<UserDto> readAll() {
    return userDao.readAll();
  }

  @Override
  public TagDto takeMostWidelyTagFromUserWithHighestCostOrders() {
    return userDao
        .takeMostWidelyTagFromUserWithHighestCostOrders();
  }
}
