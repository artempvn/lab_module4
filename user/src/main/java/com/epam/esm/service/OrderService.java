package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderWithCertificatesDto;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;

/** The interface Order service. */
public interface OrderService {

  /**
   * Create order dto with certificates with tags for creation.
   *
   * @param order the order
   * @return the order dto with certificates with tags for creation
   */
  OrderWithCertificatesWithTagsForCreationDto create(
      OrderWithCertificatesWithTagsForCreationDto order);

  /**
   * Read all by user page data.
   *
   * @param userId the user id
   * @param parameter the parameter of pagination
   * @return the page data
   */
  PageData<OrderDto> readAllByUser(long userId, PaginationParameter parameter);

  /**
   * Read order by user order dto with certificates.
   *
   * @param userId the user id
   * @param orderId the order id
   * @return the order dto with certificates
   */
  OrderWithCertificatesDto readOrderByUser(long userId, long orderId);
}
