package com.epam.esm.entity;

import java.util.List;

public class SqlData {

  private String request;
  private List<Object> args;

  public SqlData(String request, List<Object> args) {
    this.request = request;
    this.args = args;
  }

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  public List<Object> getArgs() {
    return args;
  }

  public void setArgs(List<Object> args) {
    this.args = args;
  }
}
