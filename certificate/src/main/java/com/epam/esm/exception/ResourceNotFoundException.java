package com.epam.esm.exception;

import java.util.function.Supplier;

public class ResourceNotFoundException extends ResourceException {

  public ResourceNotFoundException(String message, Long resourceId) {
    super(message, resourceId);
  }

  public static Supplier<ResourceException> notFoundWithCertificateId(Long id) {
    String message = String.format("There is no certificate with id = %s", id);
    return () -> new ResourceNotFoundException(message, id);
  }

  public static Supplier<ResourceException> notFoundWithTagId(Long id) {
    String message = String.format("There is no tag with id = %s", id);
    return () -> new ResourceNotFoundException(message, id);
  }
}
