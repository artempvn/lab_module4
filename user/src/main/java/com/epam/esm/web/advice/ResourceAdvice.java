package com.epam.esm.web.advice;

import com.epam.esm.exception.ArgumentNameException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.exception.TagException;
import com.epam.esm.exception.UserException;
import com.epam.esm.exception.UserForbiddenException;
import com.epam.esm.exception.UserNotAuthorizedException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ResourceAdvice {

  public static final String DELIMITER = "; ";
  private final ReloadableResourceBundleMessageSource messageSource;

  public ResourceAdvice(ReloadableResourceBundleMessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(ResourceValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleException(ResourceValidationException e) {
    String textMessage =
        messageSource.getMessage("error.notExist", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getResourceId());
    String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), e.getResourceId());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException e) {
    String textMessage =
        messageSource.getMessage("error.notExist", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getResourceId());
    String errorCode = String.format("%s%s", HttpStatus.NOT_FOUND.value(), e.getResourceId());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TagException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleException(TagException e) {
    String textMessage =
        messageSource.getMessage("error.noTags", null, LocaleContextHolder.getLocale());
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(textMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ErrorResponse> handleException(UserException e) {
    String textMessage =
        messageSource.getMessage("error.notUniqueLogin", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getUserLogin());
    String errorCode = String.format("%s", HttpStatus.CONFLICT.value());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotAuthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ErrorResponse> handleException(UserNotAuthorizedException e) {
    String textMessage =
        messageSource.getMessage(
            "error.notCorrectLoginData", null, LocaleContextHolder.getLocale());
    String errorCode = String.format("%s", HttpStatus.UNAUTHORIZED.value());
    ErrorResponse response = new ErrorResponse(textMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UserForbiddenException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<ErrorResponse> handleException(UserForbiddenException e) {
    String textMessage =
        messageSource.getMessage("error.forbidden", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getUserLogin());
    String errorCode = String.format("%s", HttpStatus.FORBIDDEN.value());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ArgumentNameException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleException(ArgumentNameException e) {
    String textMessage =
        messageSource.getMessage("error.argument", null, LocaleContextHolder.getLocale());
    String errorCode = String.format("%s", HttpStatus.INTERNAL_SERVER_ERROR.value());
    ErrorResponse response = new ErrorResponse(textMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleException(BindException e) {
    String errorMessage =
        (e.getBindingResult().getFieldErrors())
            .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(DELIMITER));
    String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), e.getErrorCount());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ResponseEntity<ErrorResponse> handleException(HttpRequestMethodNotSupportedException e) {
    String errorCode = String.format("%s", HttpStatus.METHOD_NOT_ALLOWED.value());
    String textMessage =
        messageSource.getMessage("error.methodNotSupported", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getMethod());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e) {
    String textMessage =
        messageSource.getMessage(
            "error.methodArgumentTypeMismatch", null, LocaleContextHolder.getLocale());
    String errorMessage = String.format("%s %s", textMessage, e.getName());
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(errorMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
    String textMessage =
        messageSource.getMessage("error.notReadable", null, LocaleContextHolder.getLocale());
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(textMessage, errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
