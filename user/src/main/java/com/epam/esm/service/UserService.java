package com.epam.esm.service;

import com.epam.esm.dto.*;

/** The interface User service. */
public interface UserService {

  /**
   * Read user dto with orders.
   *
   * @param id the id
   * @return the user dto with orders
   */
  UserDtoWithOrders read(long id);

  /**
   * Read all page data.
   *
   * @param parameter the parameter
   * @return the page data
   */
  PageData<UserDto> readAll(PaginationParameter parameter);

  /**
   * Take most widely tag from user with highest cost orders tag dto.
   *
   * @return the tag dto
   */
  TagDto takeMostWidelyTagFromUserWithHighestCostOrders();
}
