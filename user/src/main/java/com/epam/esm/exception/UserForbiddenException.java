package com.epam.esm.exception;

import java.util.function.Supplier;

public class UserForbiddenException extends RuntimeException {
  private final String userLogin;

  public UserForbiddenException(String message, String userLogin) {
    super(message);
    this.userLogin = userLogin;
  }

  public static Supplier<UserForbiddenException> forbidden(String login) {
    String message = String.format("Forbidden for user with login = %s", login);
    return () -> new UserForbiddenException(message, login);
  }

  public String getUserLogin() {
    return userLogin;
  }
}
