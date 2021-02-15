package com.epam.esm.service.impl;

import com.epam.esm.dto.LoginData;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.KeycloakService;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakServiceImpl implements KeycloakService {

  @Value("${keycloak.auth-server-url}")
  private String serverUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.resource}")
  private String clientId;

  @Value("${keycloak.credentials.secret}")
  private String secret;

  @Override
  public UserRepresentation createUserRepresentation(UserDto user) {
    UserRepresentation kcUser = new UserRepresentation();
    kcUser.setUsername(user.getLogin());
    CredentialRepresentation credentialRepresentation =
        createPasswordCredentials(user.getPassword());
    kcUser.setCredentials(List.of(credentialRepresentation));
    kcUser.setFirstName(user.getName());
    kcUser.setLastName(user.getSurname());
    kcUser.setEnabled(true);
    kcUser.setEmailVerified(false);
    return kcUser;
  }

  @Override
  public Keycloak createUserKeycloak(LoginData loginData) {
    return KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .grantType(OAuth2Constants.PASSWORD)
        .username(loginData.getLogin())
        .password(loginData.getPassword())
        .clientId(clientId)
        .clientSecret(secret)
        .build();
  }

  CredentialRepresentation createPasswordCredentials(String password) {
    CredentialRepresentation passwordCredentials = new CredentialRepresentation();
    passwordCredentials.setTemporary(false);
    passwordCredentials.setType(CredentialRepresentation.PASSWORD);
    passwordCredentials.setValue(password);
    return passwordCredentials;
  }
}
