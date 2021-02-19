package com.epam.esm.exception;

public class NotAuthorizedException extends RuntimeException {

  public NotAuthorizedException(String message) {
    super(message);
  }

  public static NotAuthorizedException notCorrectLoginData() {
    String message = "Login/password is not correct";
    return new NotAuthorizedException(message);
  }
}
