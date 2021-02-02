package com.epam.esm.dao.impl;

import com.epam.esm.dao.ReflectionService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReflectionServiceImpl implements ReflectionService {

  @Override
  public List<Field> takeNotNullFields(Object obj, Collection<String> requiredFields) {
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();
    return Arrays.stream(fields)
        .filter(field -> requiredFields.contains(field.getName()))
        .filter(field -> takeValueFromField(field, obj).isPresent())
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Object> takeValueFromField(Field field, Object inputObject) {
    field.setAccessible(true);
    Object fieldValue;
    try {
      fieldValue = field.get(inputObject);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("impossible exception");
    }
    return Optional.ofNullable(fieldValue);
  }

  @Override
  public <T> Optional<T> takeValueFromField(Field field, Object inputObject, Class<T> clazz) {
    return takeValueFromField(field, inputObject).map(value -> (T) value);
  }
}
