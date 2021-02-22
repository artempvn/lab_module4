package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
  private Long id;
  private String accessToken;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("LoginResponse{");
    sb.append("id=").append(id);
    sb.append(", accessToken='").append(accessToken).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
