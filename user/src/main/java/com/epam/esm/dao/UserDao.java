package com.epam.esm.dao;

import com.epam.esm.dto.UserDtoWithOrders;
import com.epam.esm.dto.UserDtoWithoutOrders;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    UserDtoWithoutOrders create (UserDtoWithoutOrders user);

    Optional<UserDtoWithOrders> read (long id);

    List<UserDtoWithoutOrders> readAll();
}
