package com.epam.esm.dao.impl;

import com.epam.esm.dao.CriteriaHandler;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.dto.SortParam;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CriteriaHandlerImpl implements CriteriaHandler {

  public enum CompareType {
    EQUALS,
    CONTAINS;
  }

  private static final List<String> CERTIFICATE_FIELD_NAMES =
      List.of("name", "description", "price", "duration", "lastUpdateDate");
  private static final Map<String, String> SORTING_FIELD_NAMES =
      Map.of("date", "createDate", "name", "name");
  private static final Map<String, CompareType> FILTER_REQUEST_FIELDS =
      Map.of(
          //          "tag",  TODO update method later with tag
          //          CompareType.EQUALS,
          "name", CompareType.CONTAINS, "description", CompareType.CONTAINS);

  private static final Map<
          SortParam.SortingType, BiFunction<CriteriaBuilder, Path<Certificate>, Order>>
      SORT_BUILDER =
          Map.of(
              SortParam.SortingType.ASC,
              CriteriaBuilder::asc,
              SortParam.SortingType.DESC,
              CriteriaBuilder::desc);

  @Override
  public CriteriaUpdate<Certificate> updateWithNotNullFields(
      CriteriaBuilder builder, Certificate certificate) {
    CriteriaUpdate<Certificate> criteria = builder.createCriteriaUpdate(Certificate.class);
    Root<Certificate> root = criteria.from(Certificate.class);

    List<Field> notNullFields = takeNotNullFields(certificate, CERTIFICATE_FIELD_NAMES);
    criteria = addCriteriaValuableFields(certificate, criteria, root, notNullFields);

    criteria.where(builder.equal(root.get("id"), certificate.getId()));
    return criteria;
  }

  @Override
  public CriteriaQuery<Certificate> filterWithParameters(
      CriteriaBuilder builder, CertificatesRequest request) {
    CriteriaQuery<Certificate> criteria = builder.createQuery(Certificate.class);
    Root<Certificate> root = criteria.from(Certificate.class);

    List<Field> notNullFilterFields = takeNotNullFields(request, FILTER_REQUEST_FIELDS.keySet());
    Predicate[] arrayOfPredicates =
        takeFilteringPredicatesAsArray(builder, request, root, notNullFilterFields);
    criteria.where(arrayOfPredicates);

    SortParam param = request.getSort();
    if (param != null) {
      List<Field> notNullSortingFields = takeNotNullFields(param, SORTING_FIELD_NAMES.keySet());
      List<Order> orders = takeSortingOrders(builder, root, param, notNullSortingFields);
      criteria.orderBy(orders);
    }

    return criteria;
  }

  List<Order> takeSortingOrders(
      CriteriaBuilder builder,
      Root<Certificate> root,
      SortParam param,
      List<Field> notNullSortingFields) {
    return notNullSortingFields.stream()
        .map(takeFieldOrderFunction(builder, root, param))
        .collect(Collectors.toList());
  }

  Function<Field, Order> takeFieldOrderFunction(
      CriteriaBuilder builder, Root<Certificate> root, SortParam param) {
    return field -> {
      SortParam.SortingType sortingType =
          takeValueFromField(field, param, SortParam.SortingType.class).orElseThrow();

      String fieldName = field.getName();
      String sortingFieldName = SORTING_FIELD_NAMES.get(fieldName);
      Path<Certificate> path = root.get(sortingFieldName);

      return SORT_BUILDER.get(sortingType).apply(builder, path);
    };
  }

  Predicate[] takeFilteringPredicatesAsArray(
      CriteriaBuilder builder,
      CertificatesRequest request,
      Root<Certificate> root,
      List<Field> notNullFilterFields) {
    List<Predicate> predicates =
        takeFilteringPredicates(builder, request, root, notNullFilterFields);

    Predicate[] arrayOfPredicates = new Predicate[predicates.size()];
    predicates.toArray(arrayOfPredicates);

    return arrayOfPredicates;
  }

  List<Predicate> takeFilteringPredicates(
      CriteriaBuilder builder,
      CertificatesRequest request,
      Root<Certificate> root,
      List<Field> notNullFilterFields) {
    return notNullFilterFields.stream()
        .map(takeFieldPredicateFunction(builder, request, root))
        .collect(Collectors.toList());
  }

  Function<Field, Predicate> takeFieldPredicateFunction(
      CriteriaBuilder builder, CertificatesRequest request, Root<Certificate> root) {
    return field -> {
      String fieldName = field.getName();
      String value = takeValueFromField(field, request).orElseThrow().toString();
      return builder.like(root.get(fieldName), String.format("%%%s%%", value));
    };
  }

  List<Field> takeNotNullFields(Object obj, Collection<String> requiredFields) {
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();
    return Arrays.stream(fields)
        .filter(field -> requiredFields.contains(field.getName()))
        .filter(field -> takeValueFromField(field, obj).isPresent())
        .collect(Collectors.toList());
  }

  Optional<Object> takeValueFromField(Field field, Object inputObject) {
    field.setAccessible(true);
    Object fieldValue;
    try {
      fieldValue = field.get(inputObject);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("impossible exception");
    }
    return Optional.ofNullable(fieldValue);
  }

  <T> Optional<T> takeValueFromField(Field field, Object inputObject, Class<T> clazz) {
    return takeValueFromField(field, inputObject).map(value -> (T) value);
  }

  CriteriaUpdate<Certificate> addCriteriaValuableFields(
      Certificate certificate,
      CriteriaUpdate<Certificate> criteria,
      Root<Certificate> root,
      List<Field> notNullFields) {
    for (Field field : notNullFields) {
      String fieldName = field.getName();
      Object value = takeValueFromField(field, certificate).orElseThrow();
      criteria.set(root.get(fieldName), value);
    }
    return criteria;
  }
}
