package com.epam.esm.dto;

import javax.validation.constraints.NotEmpty;

public class LoginData {

  @NotEmpty private String login;
  @NotEmpty private String password;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
