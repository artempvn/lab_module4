package com.epam.esm.dao;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoWithCertificates;
import com.epam.esm.dto.OrderDtoWithCertificatesWithTagsForCreation;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

  OrderDtoWithCertificatesWithTagsForCreation create(OrderDtoWithCertificatesWithTagsForCreation order);

  List<OrderDto> readAllByUser(long userId);

  Optional<OrderDtoWithCertificates> readOrderByUser(long userId, long orderId);
}
