package com.epam.esm.dao;

import com.epam.esm.dto.*;

import java.util.Optional;

/** The interface User dao. */
public interface UserDao {

  /**
   * Create user dto.
   *
   * @param user the user
   * @return the user dto
   */
  UserDto create(UserDto user);

  /**
   * Read optional.
   *
   * @param id the id
   * @return the optional
   */
  Optional<UserDtoWithOrders> read(long id);

  /**
   * Read without orders optional.
   *
   * @param id the id
   * @return the optional
   */
  Optional<UserDto> readWithoutOrders(long id);

  /**
   * Read all list.
   *
   * @param parameter the parameter
   * @return the list
   */
  PageData<UserDto> readAll(PaginationParameter parameter);

  /**
   * Take most widely tag from user with highest cost orders tag dto.
   *
   * @return the tag dto
   */
  TagDto takeMostWidelyTagFromUserWithHighestCostOrders();
}
