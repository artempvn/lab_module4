package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoFull;
import com.epam.esm.dto.OrderDtoFullCreation;

import java.util.List;

public interface OrderService {

  OrderDtoFullCreation create(OrderDtoFullCreation order);

  List<OrderDto> readAllByUser(long userId);

  OrderDtoFull readOrderByUser(long userId, long orderId);
}
