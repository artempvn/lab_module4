package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoFull;

public interface OrderService {

    OrderDtoFull create (OrderDtoFull order);
}
