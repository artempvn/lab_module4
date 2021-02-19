package com.epam.esm.service;

import com.epam.esm.dto.LoginData;
import com.epam.esm.dto.UserDto;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {

  UserRepresentation createUserRepresentation(UserDto user);

  Keycloak createUserKeycloak(LoginData loginData);
}
