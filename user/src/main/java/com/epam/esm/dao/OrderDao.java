package com.epam.esm.dao;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Order;

import java.util.Optional;

/** The interface Order dao. */
public interface OrderDao {

  /**
   * Create order dto with certificates with tags for creation.
   *
   * @param order the order
   * @return the order dto with certificates with tags for creation
   */
  Order create(Order order);

  /**
   * Read all by user page data.
   *
   * @param userId the user id
   * @param parameter the parameter of pagination
   * @return the page data
   */
  PageData<Order> readAllByUser(long userId, PaginationParameter parameter);

  /**
   * Read order by user optional.
   *
   * @param userId the user id
   * @param orderId the order id
   * @return the optional
   */
  Optional<Order> readOrderByUser(long userId, long orderId);
}
