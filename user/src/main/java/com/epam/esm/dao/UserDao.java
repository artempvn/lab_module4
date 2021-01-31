package com.epam.esm.dao;

import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDtoWithOrders;
import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserDao {

  UserDto create(UserDto user);

  Optional<UserDtoWithOrders> read(long id);

  Optional<UserDto> readWithoutOrders(long id);

  List<UserDto> readAll();

  TagDto takeMostWidelyTagFromUserWithHighestCostOrders();
}
