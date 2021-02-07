package com.epam.esm.dao;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** The interface Reflection service. */
public interface ReflectionService {

  /**
   * Take not null fields list.
   *
   * @param obj the obj
   * @param requiredFields the required fields
   * @return the list of not null fields
   */
  List<Field> takeNotNullFields(Object obj, Collection<String> requiredFields);

  /**
   * Take value from field optional.
   *
   * @param field the field
   * @param inputObject the input object
   * @return the optional of field's value
   */
  Optional<Object> takeValueFromField(Field field, Object inputObject);

  /**
   * Take value from field optional.
   *
   * @param <T> the type parameter
   * @param field the field
   * @param inputObject the input object
   * @param clazz the clazz
   * @return the optional of field's value
   */
  <T> Optional<T> takeValueFromField(Field field, Object inputObject, Class<T> clazz);
}
