package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoWithCertificates;
import com.epam.esm.dto.OrderDtoWithCertificatesWithTagsForCreation;
import com.epam.esm.dto.PaginationParameter;

import java.util.List;

/** The interface Order service. */
public interface OrderService {

  /**
   * Create order dto with certificates with tags for creation.
   *
   * @param order the order
   * @return the order dto with certificates with tags for creation
   */
  OrderDtoWithCertificatesWithTagsForCreation create(
      OrderDtoWithCertificatesWithTagsForCreation order);

  /**
   * Read all by user list.
   *
   * @param userId the user id
   * @param parameter the parameter
   * @return the list
   */
  List<OrderDto> readAllByUser(long userId, PaginationParameter parameter);

  /**
   * Read order by user order dto with certificates.
   *
   * @param userId the user id
   * @param orderId the order id
   * @return the order dto with certificates
   */
  OrderDtoWithCertificates readOrderByUser(long userId, long orderId);
}
