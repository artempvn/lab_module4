package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserWithOrdersDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public UserWithOrdersDto read(long id) {
    Optional<User> user = userDao.read(id);
    return user.map(UserWithOrdersDto::new)
        .orElseThrow(ResourceNotFoundException.notFoundWithUser(id));
  }

  @Override
  public PageData<UserDto> readAll(PaginationParameter parameter) {
    PageData<User> pageData = userDao.readAll(parameter);
    long numberOfElements = pageData.getNumberOfElements();
    long numberOfPages = pageData.getNumberOfPages();
    List<UserDto> users =
        pageData.getContent().stream().map(UserDto::new).collect(Collectors.toList());
    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, users);
  }

  @Override
  public TagDto takeMostWidelyTagFromUserWithHighestCostOrders() {
    return new TagDto(userDao.takeMostWidelyTagFromUserWithHighestCostOrders());
  }
}
