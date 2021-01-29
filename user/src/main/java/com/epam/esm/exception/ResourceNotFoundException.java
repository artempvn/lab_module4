package com.epam.esm.exception;

import java.util.function.Supplier;

public class ResourceNotFoundException extends ResourceException {

  public ResourceNotFoundException(String message, Long resourceId) {
    super(message, resourceId);
  }

  public static Supplier<ResourceException> notFoundWithUser(Long id) {
    String message = String.format("There is no user with id = %s", id);
    return () -> new ResourceNotFoundException(message, id);
  }

  public static Supplier<ResourceException> notFoundWithOrder(Long id) {
    String message = String.format("There is no order with id = %s", id);
    return () -> new ResourceNotFoundException(message, id);
  }
}
