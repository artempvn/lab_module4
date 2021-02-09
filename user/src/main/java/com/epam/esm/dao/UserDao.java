package com.epam.esm.dao;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.Optional;

/** The interface User dao. */
public interface UserDao {

  /**
   * Create user dto.
   *
   * @param user the user
   * @return the user dto
   */
  User create(User user);

  /**
   * Read optional.
   *
   * @param id the id
   * @return the optional
   */
  Optional<User> read(long id);

  /**
   * Read without orders optional.
   *
   * @param id the id
   * @return the optional
   */
  Optional<User> readWithoutOrders(long id);

  /**
   * Read all page data.
   *
   * @param parameter the parameter of pagination
   * @return the page data
   */
  PageData<User> readAll(PaginationParameter parameter);

  /**
   * Take most widely tag from user with highest cost orders tag dto.
   *
   * @return the tag dto
   */
  Tag takeMostWidelyTagFromUserWithHighestCostOrders();
}
