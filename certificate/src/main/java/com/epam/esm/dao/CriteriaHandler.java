package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificatesRequest;
import com.epam.esm.entity.SortParam;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CriteriaHandler {

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

  public CriteriaUpdate<Certificate> updateWithNotNullFields(
      CriteriaBuilder builder, Certificate certificate) {
    CriteriaUpdate<Certificate> criteria = builder.createCriteriaUpdate(Certificate.class);
    Root<Certificate> root = criteria.from(Certificate.class);

    List<Field> notNullFields = takeNotNullFields(certificate, CERTIFICATE_FIELD_NAMES);
    notNullFields.forEach(
        field ->
            criteria.set(
                root.get(field.getName()), takeValueFromField(field, certificate).orElseThrow()));

    criteria.where(builder.equal(root.get("id"), certificate.getId()));
    return criteria;
  }

  public CriteriaQuery<Certificate> filterWithParameters(
      CriteriaBuilder builder, CertificatesRequest request) {
    CriteriaQuery<Certificate> criteria = builder.createQuery(Certificate.class);
    Root<Certificate> root = criteria.from(Certificate.class);

    List<Field> notNullFilterFields = takeNotNullFields(request, FILTER_REQUEST_FIELDS.keySet());
    List<Predicate> predicates =
        notNullFilterFields.stream()
            .map(
                field ->
                    builder.like(
                        root.get(field.getName()),
                        String.format(
                            "%%%s%%", takeValueFromField(field, request).orElseThrow().toString())))
            .collect(Collectors.toList());
    Predicate[] arrayOfPredicates = new Predicate[notNullFilterFields.size()];
    predicates.toArray(arrayOfPredicates);

    List<Order> orders = Collections.emptyList();
    SortParam param = request.getSort();
    if (param != null) {
      List<Field> notNullSortingFields = takeNotNullFields(param, SORTING_FIELD_NAMES.keySet());

      orders =
          notNullSortingFields.stream()
              .map(
                  field -> {
                    SortParam.SortingType sortingType =
                        (SortParam.SortingType) takeValueFromField(field, param).orElseThrow();
                    switch (sortingType) {
                      case ASC:
                        return builder.asc(root.get(SORTING_FIELD_NAMES.get(field.getName())));
                      case DESC:
                        return builder.desc(root.get(SORTING_FIELD_NAMES.get(field.getName())));
                      default:
                        throw new IllegalArgumentException(
                            "There is no such sorting type provided");
                    }
                  })
              .collect(Collectors.toList());
    }

    criteria.where(arrayOfPredicates).orderBy(orders);
    return criteria;
  }

  List<Field> takeNotNullFields(Object obj, Collection requaredFields) {
    Class clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();
    return Arrays.stream(fields)
        .filter(field -> requaredFields.contains(field.getName()))
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
}
