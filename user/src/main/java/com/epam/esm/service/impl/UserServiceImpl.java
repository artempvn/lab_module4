package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserWithOrdersDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.UserException;
import com.epam.esm.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  private final UserDao userDao;
  private final Keycloak keycloak;

  public UserServiceImpl(UserDao userDao, Keycloak keycloak) {
    this.userDao = userDao;
    this.keycloak = keycloak;
  }

  @Override
  public UserWithOrdersDto read(long id) {
    Optional<User> user = userDao.read(id);
    return user.map(UserWithOrdersDto::new)
        .orElseThrow(ResourceNotFoundException.notFoundWithUser(id));
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
    CredentialRepresentation credentialRepresentation =
        createPasswordCredentials(userDto.getPassword());

    UserRepresentation kcUser = new UserRepresentation();
    kcUser.setUsername(userDto.getLogin());
    kcUser.setCredentials(List.of(credentialRepresentation));
    kcUser.setFirstName(userDto.getName());
    kcUser.setLastName(userDto.getSurname());
    kcUser.setEnabled(true);
    kcUser.setEmailVerified(false);

    User user = new User(userDto);
    try (Response response = usersResource.create(kcUser)) {
      Optional<String> headerLocation =
          Optional.ofNullable(response.getHeaderString(HEADER_LOCATION));
      String generatedUserId = headerLocation
              .map(location -> location.substring(location.lastIndexOf(SEPARATOR) + 1))
              .orElseThrow(UserException.loginIsNotUnique(userDto.getLogin()));
      user.setForeignId(generatedUserId);
    }

    User createdUser = userDao.create(user);
    return new UserDto(createdUser);
  }

  private static CredentialRepresentation createPasswordCredentials(String password) {
    CredentialRepresentation passwordCredentials = new CredentialRepresentation();
    passwordCredentials.setTemporary(false);
    passwordCredentials.setType(CredentialRepresentation.PASSWORD);
    passwordCredentials.setValue(password);
    return passwordCredentials;
  }
}
