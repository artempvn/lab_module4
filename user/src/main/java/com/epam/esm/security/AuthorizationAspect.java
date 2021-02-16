package com.epam.esm.security;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ArgumentNameException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.UserForbiddenException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@Aspect
@Component
public class AuthorizationAspect {
  private final AccessToken accessToken;
  private final UserDao userDao;

  public AuthorizationAspect(AccessToken accessToken, UserDao userDao) {
    this.accessToken = accessToken;
    this.userDao = userDao;
  }

  @Before("@annotation(AuthorizeAccess)")
  public void authorize(JoinPoint jp) {
    MethodSignature signature = (MethodSignature) jp.getSignature();
    Method method = signature.getMethod();
    AuthorizeAccess annotation = method.getAnnotation(AuthorizeAccess.class);

    CodeSignature codeSignature = (CodeSignature) jp.getSignature();
    String[] parameterNames = codeSignature.getParameterNames();
    int argUserId = Arrays.asList(parameterNames).indexOf(annotation.value());
    if (argUserId == -1) {
      throw new ArgumentNameException("There is no argument  with such name");
    }
    Long userId = (Long) jp.getArgs()[argUserId];

    User user =
        userDao.read(userId).orElseThrow(ResourceNotFoundException.notFoundWithUser(userId));
    String foreignIdOfExistingUser = user.getForeignId();

    String foreignIdOfLoginUser = accessToken.getSubject();
    AccessToken.Access realmAccess = accessToken.getRealmAccess();
    Set<String> roles = realmAccess.getRoles();

    if (!roles.contains(Role.ADMIN.name())
        && !foreignIdOfLoginUser.equals(foreignIdOfExistingUser)) {
      throw UserForbiddenException.forbidden(accessToken.getPreferredUsername()).get();
    }
  }
}
