package com.epam.esm.service;

import com.epam.esm.dto.UserDtoWithOrders;
import com.epam.esm.dto.UserDto;

import java.util.List;

public interface UserService {

  UserDtoWithOrders read(long id);

  List<UserDto> readAll();
}
