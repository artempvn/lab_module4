package com.epam.esm.dao;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoFull;
import com.epam.esm.dto.OrderDtoFullCreation;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

  OrderDtoFullCreation create(OrderDtoFullCreation order);

  List<OrderDto> readAllByUser(long userId);

  Optional<OrderDtoFull> readOrderByUser(long userId, long orderId);
}
