package com.epam.esm.dao;

import com.epam.esm.dto.UserDtoFull;
import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserDao {

  UserDto create(UserDto user);

  Optional<UserDtoFull> read(long id);

  List<UserDto> readAll();
}
