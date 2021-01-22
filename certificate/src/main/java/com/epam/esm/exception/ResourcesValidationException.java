package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourcesValidationException extends RuntimeException {
  private final Long tagId;
  private final Long certificateId;

  public Long getTagId() {
    return tagId;
  }

  public Long getCertificateId() {
    return certificateId;
  }

  public static Supplier<ResourcesValidationException> withIds(Long tagId, Long certificateId) {
    String message =
        String.format("There is no tag id = %s in certificate id = %s", tagId, certificateId);
    return () -> new ResourcesValidationException(message, tagId, certificateId);
  }

  public ResourcesValidationException(String message, Long tagId, Long certificateId) {
    super(message);
    this.tagId = tagId;
    this.certificateId = certificateId;
  }
}
