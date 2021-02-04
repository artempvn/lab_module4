package com.epam.esm.dao.impl;

import com.epam.esm.dao.CriteriaHandler;
import com.epam.esm.dao.PredicateFactory;
import com.epam.esm.dao.ReflectionService;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.dto.SortParam;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CriteriaHandlerImpl implements CriteriaHandler {
  private static final List<String> CERTIFICATE_FIELD_NAMES =
      List.of("name", "description", "price", "duration", "lastUpdateDate");
  private static final Map<String, String> SORTING_FIELD_NAMES =
      Map.of("date", "createDate", "name", "name");

  private static final Map<
          SortParam.SortingType, BiFunction<CriteriaBuilder, Path<Certificate>, Order>>
      SORT_BUILDER =
          Map.of(
              SortParam.SortingType.ASC,
              CriteriaBuilder::asc,
              SortParam.SortingType.DESC,
              CriteriaBuilder::desc);

  private final ReflectionService reflectionService;
  private final List<PredicateFactory> predicateFactories;

  public CriteriaHandlerImpl(
      ReflectionService reflectionService, List<PredicateFactory> predicateFactories) {
    this.reflectionService = reflectionService;
    this.predicateFactories = predicateFactories;
  }

  @Override
  public CriteriaUpdate<Certificate> updateWithNotNullFields(
      CriteriaBuilder builder, Certificate certificate) {
    CriteriaUpdate<Certificate> criteria = builder.createCriteriaUpdate(Certificate.class);
    Root<Certificate> root = criteria.from(Certificate.class);

    List<Field> notNullFields =
        reflectionService.takeNotNullFields(certificate, CERTIFICATE_FIELD_NAMES);
    criteria = addCriteriaValuableFields(certificate, criteria, root, notNullFields);

    criteria.where(builder.equal(root.get("id"), certificate.getId()));
    return criteria;
  }

  @Override
  public CriteriaQuery<Certificate> filterWithParameters(
      CriteriaBuilder builder, CertificatesRequest request) {
    CriteriaQuery<Certificate> criteria = builder.createQuery(Certificate.class);
    Root<Certificate> root = criteria.from(Certificate.class);

    Predicate[] predicates =
        predicateFactories.stream()
            .map(factory -> factory.buildPredicates(builder, request, root))
            .flatMap(List::stream)
            .toArray(Predicate[]::new);
    criteria.where(predicates);

    SortParam param = request.getSort();
    if (param != null) {
      List<Field> notNullSortingFields =
          reflectionService.takeNotNullFields(param, SORTING_FIELD_NAMES.keySet());
      List<Order> orders = takeSortingOrders(builder, root, param, notNullSortingFields);
      criteria.orderBy(orders);
    }

    return criteria;
  }

  Predicate[] convertListPredicatesToArray(List<Predicate> predicates) {
    Predicate[] arrayOfPredicates = new Predicate[predicates.size()];
    predicates.toArray(arrayOfPredicates);
    return arrayOfPredicates;
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
          reflectionService
              .takeValueFromField(field, param, SortParam.SortingType.class)
              .orElseThrow();

      String fieldName = field.getName();
      String sortingFieldName = SORTING_FIELD_NAMES.get(fieldName);
      Path<Certificate> path = root.get(sortingFieldName);

      return SORT_BUILDER.get(sortingType).apply(builder, path);
    };
  }

  CriteriaUpdate<Certificate> addCriteriaValuableFields(
      Certificate certificate,
      CriteriaUpdate<Certificate> criteria,
      Root<Certificate> root,
      List<Field> notNullFields) {
    for (Field field : notNullFields) {
      String fieldName = field.getName();
      Object value = reflectionService.takeValueFromField(field, certificate).orElseThrow();
      criteria.set(root.get(fieldName), value);
    }
    return criteria;
  }
}
