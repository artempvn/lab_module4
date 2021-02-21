package com.epam.esm.security;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

/** The type Security decision maker. */
@Component("authorizationDecisionMaker")
public class SecurityDecisionMaker {
  private final AccessToken accessToken;
  private final UserDao userDao;

  /**
   * Instantiates a new Security decision maker.
   *
   * @param accessToken the access token
   * @param userDao the user dao
   */
  public SecurityDecisionMaker(AccessToken accessToken, UserDao userDao) {
    this.accessToken = accessToken;
    this.userDao = userDao;
  }

  /**
   * Comparing foreign ids of users.
   *
   * @param userId the user id
   * @return the result of comparing foreign id of presented user with foreign id of logged user
   */
  public boolean match(long userId) {
    String foreignIdOfLoginUser = accessToken.getSubject();

    Optional<User> user = userDao.read(userId);
    String foreignIdOfExistingUser =
        user.map(User::getForeignId)
            .orElseThrow(ResourceNotFoundException.notFoundWithUser(userId));

    return foreignIdOfLoginUser.equals(foreignIdOfExistingUser);
  }
}
