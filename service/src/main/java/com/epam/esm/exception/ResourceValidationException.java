package com.epam.esm.exception;

public class ResourceValidationException extends ResourceException {

  public ResourceValidationException(String message, Long resourceId) {
    super(message, resourceId);
  }
}
