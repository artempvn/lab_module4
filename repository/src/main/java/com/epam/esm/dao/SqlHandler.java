package com.epam.esm.dao;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

//@Component
//@PropertySource("classpath:certificate_column.properties")
public class SqlHandler {
//  private static final String SELECT =
//      "SELECT gift_certificates.id,gift_certificates.name,description,price,duration,create_date,last_update_date FROM gift_certificates ";
//  private static final String JOIN =
//      " JOIN certificates_tags ON certificate_id=gift_certificates.id JOIN tag ON tag_id=tag.id ";
//  private static final String TABLE_NAME_KEY = "table_name";
//  public static final String ID_FIELD = "id";
//  public static final String EMPTY_STRING = "";
//  public static final String TAG_NAME = "tagName";
//  public static final String DELIMITER = ", ";
//  public static final String AND = " AND ";
//
//  private final Environment environment;
//
//  public SqlHandler(Environment environment) {
//    this.environment = environment;
//  }
//
//  public SqlData generateSqlDataForReadAllRequest(CertificatesRequest parameter) {
//    StringBuilder request = new StringBuilder(SELECT);
//    List<Object> args = new ArrayList<>();
//    if (parameter != null) {
//      String joinPart = EMPTY_STRING;
//      if (parameter.getTagName() != null) {
//        joinPart = JOIN;
//      }
//      List<Field> notNullFields = takeNotNullFields(parameter);
//      String wherePart = generateRequestParametersForWherePart(notNullFields);
//      args.addAll(takeFieldsValueWithWrapperForRequest(notNullFields, parameter));
//      SortParam param = parameter.getSort();
//      String orderByPart = EMPTY_STRING;
//      if (param != null) {
//        List<Field> notNullFieldsSort = takeNotNullFields(param);
//        orderByPart = generateRequestParametersForOrderByPart(notNullFieldsSort, param);
//      }
//      request.append(makeSelectRequest(joinPart, wherePart, orderByPart));
//    }
//    return new SqlData(request.toString(), args);
//  }
//
//  public SqlData generateSqlDataForUpdateRequest(CertificatePatch inputCertificate) {
//    List<Field> notNullFields = takeNotNullFields(inputCertificate);
//    String requestParameters = generateRequestParametersForUpdate(notNullFields);
//    List<Object> args = takeFieldsValue(notNullFields, inputCertificate);
//    args.add(takeIdValue(inputCertificate).orElseThrow());
//    String sqlRequest = makePatchRequestWithValuableFields(requestParameters);
//    return new SqlData(sqlRequest, args);
//  }
//
//  List<Field> takeNotNullFields(Object obj) {
//    Class clazz = obj.getClass();
//    Field[] fields = clazz.getDeclaredFields();
//    return Arrays.stream(fields)
//        .filter(field -> Objects.nonNull(environment.getProperty(field.getName())))
//        .filter(field -> takeValueFromField(field, obj).isPresent())
//        .collect(Collectors.toList());
//  }
//
//  Optional<Object> takeIdValue(Object obj) {
//    Class clazz = obj.getClass();
//    Field[] fields = clazz.getDeclaredFields();
//    return Arrays.stream(fields)
//        .filter(field -> (field.getName().equals(ID_FIELD)))
//        .findFirst()
//        .map(field -> takeValueFromField(field, obj).orElseThrow());
//  }
//
//  List<Object> takeFieldsValue(List<Field> notNullFields, Object inputObject) {
//    return notNullFields.stream()
//        .map(field -> takeValueFromField(field, inputObject).orElseThrow())
//        .collect(Collectors.toList());
//  }
//
//  List<Object> takeFieldsValueWithWrapperForRequest(List<Field> notNullFields, Object inputObject) {
//    return notNullFields.stream()
//        .map(
//            field -> {
//              Object obj = takeValueFromField(field, inputObject).orElseThrow();
//              return field.getName().equals(TAG_NAME) ? obj : String.format("%%%s%%", obj);
//            })
//        .collect(Collectors.toList());
//  }
//
//  String generateRequestParametersForUpdate(List<Field> notNullFields) {
//    return notNullFields.stream()
//        .map(Field::getName)
//        .map(environment::getProperty)
//        .map(value -> String.format("%s=?", value))
//        .collect(Collectors.joining(DELIMITER));
//  }
//
//  String generateRequestParametersForWherePart(List<Field> notNullFields) {
//    return notNullFields.stream()
//        .map(Field::getName)
//        .map(environment::getProperty)
//        .collect(Collectors.joining(AND));
//  }
//
//  String generateRequestParametersForOrderByPart(List<Field> notNullFields, Object inputObject) {
//    return notNullFields.stream()
//        .map(
//            field -> {
//              String sortType = environment.getProperty(field.getName());
//              Object obj = takeValueFromField(field, inputObject).orElseThrow();
//              return String.format("%s %s", sortType, obj);
//            })
//        .collect(Collectors.joining(DELIMITER));
//  }
//
//  Optional<Object> takeValueFromField(Field field, Object inputObject) {
//    field.setAccessible(true);
//    Object fieldValue;
//    try {
//      fieldValue = field.get(inputObject);
//    } catch (IllegalAccessException e) {
//      throw new RuntimeException("impossible exception");
//    }
//    return Optional.ofNullable(fieldValue);
//  }
//
//  String makePatchRequestWithValuableFields(String fields) {
//    String tableName = environment.getProperty(TABLE_NAME_KEY);
//    return String.format("UPDATE %s SET %s WHERE id=?", tableName, fields);
//  }
//
//  String makeSelectRequest(String joinPart, String wherePart, String orderByPart) {
//    if (!wherePart.isBlank()) {
//      wherePart = String.format("WHERE %s ", wherePart);
//    }
//    if (!orderByPart.isBlank()) {
//      orderByPart = String.format("ORDER BY %s ", orderByPart);
//    }
//    return joinPart + wherePart + orderByPart;
//  }
}
