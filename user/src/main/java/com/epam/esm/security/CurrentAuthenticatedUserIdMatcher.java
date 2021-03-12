package com.epam.esm.security;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Component;

/** The type CurrentAuthenticatedUserIdMatcher. */
@Component
public class CurrentAuthenticatedUserIdMatcher {
  private final AccessToken accessToken;
  private final UserDao userDao;

  /**
   * Instantiates a new Current authenticated userId matcher.
   *
   * @param accessToken the access token
   * @param userDao the user dao
   */
  public CurrentAuthenticatedUserIdMatcher(AccessToken accessToken, UserDao userDao) {
    this.accessToken = accessToken;
    this.userDao = userDao;
  }

  /**
   * Comparing foreign ids of users, e.g. github login.
   *
   * @param userId the user id
   * @return the result of comparing foreign id of presented user with foreign id of logged user
   */
  public boolean match(long userId) {
    String foreignIdOfLoginUser = accessToken.getPreferredUsername();

    String foreignIdOfExistingUser =
        userDao
            .read(userId)
            .map(User::getForeignId)
            .orElseThrow(ResourceNotFoundException.notFoundWithUser(userId));

    return foreignIdOfLoginUser.equals(foreignIdOfExistingUser);
  }
}
