package com.epam.esm.dao;

import com.epam.esm.dto.*;

import java.util.Optional;

/** The interface Order dao. */
public interface OrderDao {

  /**
   * Create order dto with certificates with tags for creation.
   *
   * @param order the order
   * @return the order dto with certificates with tags for creation
   */
  OrderDtoWithCertificatesWithTagsForCreation create(
      OrderDtoWithCertificatesWithTagsForCreation order);

  /**
   * Read all by user page data.
   *
   * @param userId the user id
   * @param parameter the parameter
   * @return the page data
   */
  PageData<OrderDto> readAllByUser(long userId, PaginationParameter parameter);

  /**
   * Read order by user optional.
   *
   * @param userId the user id
   * @param orderId the order id
   * @return the optional
   */
  Optional<OrderDtoWithCertificates> readOrderByUser(long userId, long orderId);
}
