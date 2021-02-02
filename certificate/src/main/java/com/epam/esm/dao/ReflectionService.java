package com.epam.esm.dao;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReflectionService {

  List<Field> takeNotNullFields(Object obj, Collection<String> requiredFields);

  Optional<Object> takeValueFromField(Field field, Object inputObject);

  <T> Optional<T> takeValueFromField(Field field, Object inputObject, Class<T> clazz);
}
