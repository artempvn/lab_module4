package com.epam.esm.exception;

import java.util.function.Supplier;

public class ResourceIsBoundException extends ResourceException {

  public ResourceIsBoundException(String message, Long resourceId) {
    super(message, resourceId);
  }

  public static Supplier<ResourceException> isBound(Long id) {
    String message = String.format("Resource has bounds with another resources, id = %s", id);
    return () -> new ResourceIsBoundException(message, id);
  }
}
