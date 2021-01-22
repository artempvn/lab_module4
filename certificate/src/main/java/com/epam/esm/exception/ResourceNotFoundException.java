package com.epam.esm.exception;

public class ResourceNotFoundException extends ResourceException {

  public ResourceNotFoundException(String message, Long resourceId) {
    super(message, resourceId);
  }
}
