package com.epam.esm.dao;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Order;

import java.util.Optional;

/** The interface Order dao. */
public interface OrderDao {

  /**
   * Persist order with binding all of its certificates.
   *
   * @param order the order with certificates
   * @return saved order
   */
  Order create(Order order);

  /**
   * Read all orders related to provided user id and meet pagination parameters.
   *
   * @param userId the user id
   * @param parameter the parameter of pagination
   * @return the page data with found orders and page info
   */
  PageData<Order> readAllByUser(long userId, PaginationParameter parameter);

  /**
   * Read order by id related to provided user id.
   *
   * @param userId the user id
   * @param orderId the order id
   * @return the optional of order or empty optional if it's not exist
   */
  Optional<Order> readOrderByUser(long userId, long orderId);
}
