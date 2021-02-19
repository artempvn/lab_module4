package com.epam.esm.exception;

public class LoginAlreadyExistsException extends RuntimeException {

  private final String userLogin;

  public LoginAlreadyExistsException(String message, String userLogin) {
    super(message);
    this.userLogin = userLogin;
  }

  public static LoginAlreadyExistsException loginIsNotUnique(String login) {
    String message = String.format("Login already exists, login = %s", login);
    return new LoginAlreadyExistsException(message, login);
  }

  public String getUserLogin() {
    return userLogin;
  }
}
