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
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.UserException;
import com.epam.esm.exception.UserNotAuthorizedException;
import com.epam.esm.security.SecurityHandler;
import com.epam.esm.service.KeycloakService;
import com.epam.esm.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
  public static final String HEADER_LOCATION = "location";
  public static final String SEPARATOR = "/";

  @Value("${keycloak.realm}")
  private String realm;

  private final SecurityHandler securityHandler;
  private final UserDao userDao;
  private final Keycloak keycloak;
  private final KeycloakService keycloakService;

  public UserServiceImpl(SecurityHandler securityHandler, UserDao userDao, Keycloak keycloak, KeycloakService keycloakService) {
    this.securityHandler = securityHandler;
    this.userDao = userDao;
    this.keycloak = keycloak;
    this.keycloakService = keycloakService;
  }

  @Override
  public UserWithOrdersDto read(long id) {
    User user = userDao.read(id).orElseThrow(ResourceNotFoundException.notFoundWithUser(id));

    String foreignId=user.getForeignId();
    securityHandler.checkingAuthorization(foreignId);

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
        throw UserException.loginIsNotUnique(userDto.getLogin()).get();
      }
      String headerLocation = response.getHeaderString(HEADER_LOCATION);
      String generatedUserId = headerLocation.substring(headerLocation.lastIndexOf(SEPARATOR) + 1);
      user.setForeignId(generatedUserId);

      RoleRepresentation savedRoleRepresentation =
              keycloak.realm("certificates").roles().get(Role.USER.name()).toRepresentation();
      keycloak.realm("certificates").users().get(generatedUserId).roles().realmLevel()
              .add(List.of(savedRoleRepresentation));
    }

    User createdUser = userDao.create(user);
    return new UserDto(createdUser);
  }

  @Override
  public String login(LoginData loginData) {
   try(Keycloak userKeycloak=keycloakService.createUserKeycloak(loginData)){
     return userKeycloak.tokenManager().getAccessTokenString();
   } catch (NotAuthorizedException ex){
     throw UserNotAuthorizedException.notCorrectLoginData().get();
   }

  }
}
