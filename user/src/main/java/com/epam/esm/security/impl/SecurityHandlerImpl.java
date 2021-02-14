package com.epam.esm.security.impl;

import com.epam.esm.dto.Role;
import com.epam.esm.exception.UserForbiddenException;
import com.epam.esm.security.SecurityHandler;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SecurityHandlerImpl implements SecurityHandler {

  private final AccessToken accessToken;

  public SecurityHandlerImpl(AccessToken accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public void checkingAuthorization(String foreignIdOfExistingUser) {
    String foreignIdOfLoginUser = accessToken.getSubject();
    AccessToken.Access realmAccess = accessToken.getRealmAccess();
    Set<String> roles = realmAccess.getRoles();

    if (!roles.contains(Role.ADMIN.name())
        && !foreignIdOfLoginUser.equals(foreignIdOfExistingUser)) {
      throw UserForbiddenException.forbidden(accessToken.getPreferredUsername()).get();
    }
  }
}
