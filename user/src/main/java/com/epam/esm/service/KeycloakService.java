package com.epam.esm.service;

import com.epam.esm.dto.LoginData;
import com.epam.esm.dto.UserDto;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;

/** The interface Keycloak service. */
public interface KeycloakService {

  /**
   * Create user representation by user data.
   *
   * @param user the user
   * @return the user representation
   */
  UserRepresentation createUserRepresentation(UserDto user);

  /**
   * Create user keycloak data by login data.
   *
   * @param loginData the login data, contains login and password
   * @return the keycloak data
   */
  Keycloak createUserKeycloak(LoginData loginData);
}
