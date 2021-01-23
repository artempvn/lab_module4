package com.epam.esm.exception;

import java.util.function.Supplier;

public class ResourceException extends RuntimeException {

  private final Long resourceId;

  public Long getResourceId() {
    return resourceId;
  }

  public static Supplier<ResourceException> notFoundWithCertificateId(Long id) {
    String message = String.format("There is no certificate with id = %s", id);
    return () -> new ResourceNotFoundException(message, id);
  }

  public static Supplier<ResourceException> validationWithCertificateId(Long id) {
    String message = String.format("There is no certificate with id = %s", id);
    return () -> new ResourceValidationException(message, id);
  }

  public static Supplier<ResourceException> notFoundWithTagId(Long id) {
    String message = String.format("There is no tag with id = %s", id);
    return () -> new ResourceNotFoundException(message, id);
  }

  public static Supplier<ResourceException> validationWithTagId(Long id) {
    String message = String.format("There is no tag with id = %s", id);
    return () -> new ResourceValidationException(message, id);
  }

  public static Supplier<ResourceException> isBound(Long id) {
    String message = String.format("Resource has bounds with another resources, id = %s", id);
    return () -> new ResourceIsBoundException(message, id);
  }

  public ResourceException(String message, Long resourceId) {
    super(message);
    this.resourceId = resourceId;
  }
}
