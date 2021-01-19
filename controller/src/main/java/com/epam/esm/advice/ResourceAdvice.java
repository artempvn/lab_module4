package com.epam.esm.advice;

import com.epam.esm.entity.ErrorResponse;
import com.epam.esm.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ResourceAdvice {

  @ExceptionHandler(ResourceValidationException.class)
  public ResponseEntity<ErrorResponse> handleException(ResourceValidationException e) {
    String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), e.getResourceId());
    ErrorResponse response = new ErrorResponse(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourcesValidationException.class)
  public ResponseEntity<ErrorResponse> handleException(ResourcesValidationException e) {
    String errorCode =
        String.format(
            "%s:%s.%s", HttpStatus.BAD_REQUEST.value(), e.getTagId(), e.getCertificateId());
    ErrorResponse response = new ErrorResponse(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException e) {
    String errorCode = String.format("%s%s", HttpStatus.NOT_FOUND.value(), e.getResourceId());
    ErrorResponse response = new ErrorResponse(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
    StringBuilder errorMessage = new StringBuilder();
    (e.getBindingResult().getFieldErrors())
        .forEach(error -> errorMessage.append(String.format("%s; ", error.getDefaultMessage())));
    String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), e.getErrorCount());
    ErrorResponse response = new ErrorResponse(errorMessage.toString(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleException(HttpRequestMethodNotSupportedException e) {
    String errorCode = String.format("%s", HttpStatus.METHOD_NOT_ALLOWED.value());
    ErrorResponse response = new ErrorResponse(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e) {
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleException(BindException e) {
    String errorCode = String.format("%s", HttpStatus.BAD_REQUEST.value());
    ErrorResponse response = new ErrorResponse(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
