package com.epam.esm.security;

public interface SecurityHandler {

  void checkingAuthorization(String foreignIdOfExistingUser);
}
