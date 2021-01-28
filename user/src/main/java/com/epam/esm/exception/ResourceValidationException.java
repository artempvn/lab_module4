package com.epam.esm.exception;

import java.util.function.Supplier;

public class ResourceValidationException extends ResourceException {

  public ResourceValidationException(String message, Long resourceId) {
    super(message, resourceId);
  }

  public static Supplier<ResourceException> validationWithCertificateId(Long id) {
    String message = String.format("There is no certificate with id = %s", id);
    return () -> new ResourceValidationException(message, id);
  }

  public static Supplier<ResourceException> validationWithTagId(Long id) {
    String message = String.format("There is no tag with id = %s", id);
    return () -> new ResourceValidationException(message, id);
  }
}
