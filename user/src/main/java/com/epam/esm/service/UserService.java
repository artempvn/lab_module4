package com.epam.esm.service;

import com.epam.esm.dto.UserDtoFull;
import com.epam.esm.dto.UserDto;

import java.util.List;

public interface UserService {

  UserDtoFull read(long id);

  List<UserDto> readAll();
}
