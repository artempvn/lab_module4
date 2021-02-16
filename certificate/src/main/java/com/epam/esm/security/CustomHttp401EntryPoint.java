package com.epam.esm.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CustomHttp401EntryPoint implements AuthenticationEntryPoint {
  private final ReloadableResourceBundleMessageSource messageSource;

  public CustomHttp401EntryPoint(ReloadableResourceBundleMessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    String textMessage =
        messageSource.getMessage("error.401", null, LocaleContextHolder.getLocale());
    Map<String, Object> body = new HashMap<>();
    body.put("errorMessage", textMessage);
    body.put("errorCode", HttpStatus.UNAUTHORIZED.value());
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    new Gson()
        .toJson(body, new TypeReference<Map<String, Object>>() {}.getType(), response.getWriter());
  }
}
