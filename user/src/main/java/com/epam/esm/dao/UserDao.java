package com.epam.esm.dao;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.TagException;

import java.util.Optional;

/** The interface User dao. */
public interface UserDao {

  /**
   * Persist user.
   *
   * @param user the user
   * @return saved user
   */
  User create(User user);

  /**
   * Read user by id.
   *
   * @param id the id of user
   * @return the optional of user or empty optional if it's not exist
   */
  Optional<User> read(long id);

  /**
   * Read all users meet pagination parameters.
   *
   * @param parameter the parameter of pagination
   * @return the page data with found users and page info
   */
  PageData<User> readAll(PaginationParameter parameter);

  /**
   * Take most widely used tag from user with highest cost of all orders.
   *
   * @return most widely used tag of user with highest cost of all orders
   * @throws TagException if there is no any tag found in orders of user with highest cost of all
   *     orders
   */
  Tag takeMostWidelyTagFromUserWithHighestCostOrders();
}
