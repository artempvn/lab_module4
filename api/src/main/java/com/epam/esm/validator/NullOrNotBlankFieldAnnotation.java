package com.epam.esm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CertificatePatchValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotBlankFieldAnnotation {
  String message() default "{Certificate}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
