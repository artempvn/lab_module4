package com.epam.esm.dao;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoFull;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    OrderDtoFull create(OrderDtoFull order);

    Optional<OrderDtoFull> read(long id);

    List<OrderDto> readAll();


}
