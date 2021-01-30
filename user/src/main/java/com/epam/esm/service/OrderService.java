package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoWithCertificates;
import com.epam.esm.dto.OrderDtoWithCertificatesWithTagsForCreation;

import java.util.List;

public interface OrderService {

  OrderDtoWithCertificatesWithTagsForCreation create(OrderDtoWithCertificatesWithTagsForCreation order);

  List<OrderDto> readAllByUser(long userId);

  OrderDtoWithCertificates readOrderByUser(long userId, long orderId);
}
