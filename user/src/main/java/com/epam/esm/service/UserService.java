package com.epam.esm.service;

import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDtoWithOrders;
import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

  UserDtoWithOrders read(long id);

  List<UserDto> readAll(PaginationParameter parameter);

  TagDto takeMostWidelyTagFromUserWithHighestCostOrders();
}
