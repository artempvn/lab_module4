package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.LoginData;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.Role;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserWithOrdersDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.LoginAlreadyExistsException;
import com.epam.esm.exception.NotAuthorizedException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.KeycloakService;
import com.epam.esm.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
  public static final String HEADER_LOCATION = "location";
  public static final String SEPARATOR = "/";

  private final String realm;
  private final UserDao userDao;
  private final Keycloak keycloak;
  private final KeycloakService keycloakService;

  public UserServiceImpl(
      UserDao userDao,
      Keycloak keycloak,
      KeycloakService keycloakService,
      @Value("${keycloak.realm}") String realm) {
    this.userDao = userDao;
    this.keycloak = keycloak;
    this.keycloakService = keycloakService;
    this.realm = realm;
  }

  @Override
  @PreAuthorize(
          "hasRole('ADMIN') or @authorizationDecisionMaker.match(#userId)")
  public UserWithOrdersDto read(long userId) {
    User user =
        userDao.read(userId).orElseThrow(ResourceNotFoundException.notFoundWithUser(userId));

    return new UserWithOrdersDto(user);
  }

  @Override
  public PageData<UserDto> readAll(PaginationParameter parameter) {
    PageData<User> pageData = userDao.readAll(parameter);
    long numberOfElements = pageData.getNumberOfElements();
    long numberOfPages = pageData.getNumberOfPages();
    List<UserDto> users =
        pageData.getContent().stream().map(UserDto::new).collect(Collectors.toList());
    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, users);
  }

  @Override
  public TagDto takeMostWidelyTagFromUserWithHighestCostOrders() {
    return new TagDto(userDao.takeMostWidelyTagFromUserWithHighestCostOrders());
  }

  @Override
  public UserDto create(UserDto userDto) {
    UsersResource usersResource = keycloak.realm(realm).users();
    UserRepresentation kcUser = keycloakService.createUserRepresentation(userDto);

    User user = new User(userDto);
    try (Response response = usersResource.create(kcUser)) {
      if (response.getStatus() == HttpStatus.CONFLICT.value()) {
        throw LoginAlreadyExistsException.loginIsNotUnique(userDto.getLogin());
      }
      String headerLocation = response.getHeaderString(HEADER_LOCATION);
      String generatedUserId = headerLocation.substring(headerLocation.lastIndexOf(SEPARATOR) + 1);
      user.setForeignId(generatedUserId);

      RoleRepresentation savedRoleRepresentation =
          keycloak.realm(realm).roles().get(Role.USER.name()).toRepresentation();
      keycloak
          .realm(realm)
          .users()
          .get(generatedUserId)
          .roles()
          .realmLevel()
          .add(List.of(savedRoleRepresentation));
    }

    User createdUser = userDao.create(user);
    return new UserDto(createdUser);
  }

  @Override
  public String login(LoginData loginData) {
    try (Keycloak userKeycloak = keycloakService.createUserKeycloak(loginData)) {
      return userKeycloak.tokenManager().getAccessTokenString();
    } catch (javax.ws.rs.NotAuthorizedException ex) {
      throw NotAuthorizedException.notCorrectLoginData();
    }
  }
}
