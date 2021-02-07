package com.epam.esm.exception;

public class ResourceException extends RuntimeException {

  private final Long resourceId;

  public ResourceException(String message, Long resourceId) {
    super(message);
    this.resourceId = resourceId;
  }

  public Long getResourceId() {
    return resourceId;
  }
}
