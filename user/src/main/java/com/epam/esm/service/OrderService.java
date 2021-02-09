package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderWithCertificatesDto;
import com.epam.esm.dto.OrderWithCertificatesWithTagsForCreationDto;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;

/** The interface Order service. */
public interface OrderService {

  /**
   * Persist order with certificates with tags.
   *
   * @param order the order
   * @return saved order with certificates with tags
   */
  OrderWithCertificatesWithTagsForCreationDto create(
      OrderWithCertificatesWithTagsForCreationDto order);

  /**
   * Read all orders related to provided user id and meet pagination parameters.
   *
   * @param userId the user id
   * @param parameter the parameter of pagination
   * @return the page data with found orders and page info
   * @throws ResourceValidationException if user is not presented
   */
  PageData<OrderDto> readAllByUser(long userId, PaginationParameter parameter);

  /**
   * Read order by id related to provided user id.
   *
   * @param userId the user id
   * @param orderId the order id
   * @return found order
   * @throws ResourceNotFoundException if order is not found
   * @throws ResourceValidationException if user is not presented
   */
  OrderWithCertificatesDto readOrderByUser(long userId, long orderId);
}
