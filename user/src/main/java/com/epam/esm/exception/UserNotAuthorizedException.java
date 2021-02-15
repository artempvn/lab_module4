package com.epam.esm.exception;

import java.util.function.Supplier;

public class UserNotAuthorizedException extends RuntimeException {

  public UserNotAuthorizedException(String message) {
    super(message);
  }

  public static Supplier<UserNotAuthorizedException> notCorrectLoginData() {
    String message = "Login/password is not correct";
    return () -> new UserNotAuthorizedException(message);
  }
}
