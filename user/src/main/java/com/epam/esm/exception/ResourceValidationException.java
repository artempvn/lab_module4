package com.epam.esm.exception;

import java.util.function.Supplier;

public class ResourceValidationException extends ResourceException {

  public ResourceValidationException(String message, Long resourceId) {
    super(message, resourceId);
  }

  public static Supplier<ResourceException> validationWithUser(Long id) {
    String message = String.format("There is no user with id = %s", id);
    return () -> new ResourceValidationException(message, id);
  }

  public static Supplier<ResourceException> validationWithOrder(Long id) {
    String message = String.format("There is no order with id = %s", id);
    return () -> new ResourceValidationException(message, id);
  }
}
