package com.epam.esm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CertificatePatchValidator
    implements ConstraintValidator<NullOrNotBlankFieldAnnotation, String> {
  public static final int MAX_LENGTH = 45;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return (value == null || (!value.isBlank() && value.length() <= MAX_LENGTH));
  }
}
