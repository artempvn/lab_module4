package com.epam.esm.exception;

import java.util.function.Supplier;

public class UserException extends RuntimeException {

  private final String userLogin;

  public UserException(String message, String userLogin) {
    super(message);
    this.userLogin = userLogin;
  }

  public static Supplier<UserException> loginIsNotUnique(String login) {
    String message = String.format("Login already exists, login = %s", login);
    return () -> new UserException(message, login);
  }

  public String getUserLogin() {
    return userLogin;
  }
}
