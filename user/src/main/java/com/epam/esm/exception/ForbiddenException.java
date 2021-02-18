package com.epam.esm.exception;

public class ForbiddenException extends RuntimeException {
  private final String userLogin;

  public ForbiddenException(String message, String userLogin) {
    super(message);
    this.userLogin = userLogin;
  }

  public static ForbiddenException forbidden(String login) {
    String message = String.format("Forbidden for user with login = %s", login);
    return new ForbiddenException(message, login);
  }

  public String getUserLogin() {
    return userLogin;
  }
}
