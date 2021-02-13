package com.epam.esm.service;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserWithOrdersDto;
import com.epam.esm.exception.ResourceNotFoundException;

/** The interface User service. */
public interface UserService {

  /**
   * Read user by id.
   *
   * @param id the id of user
   * @return found user
   * @throws ResourceNotFoundException if user is not found
   */
  UserWithOrdersDto read(long id);

  /**
   * Read all users meet pagination parameters.
   *
   * @param parameter the parameter of pagination
   * @return the page data with found users and page info
   */
  PageData<UserDto> readAll(PaginationParameter parameter);

  /**
   * Take most widely used tag from user with highest cost of all orders.
   *
   * @return found tag
   */
  TagDto takeMostWidelyTagFromUserWithHighestCostOrders();

  UserDto create(UserDto user);
}
