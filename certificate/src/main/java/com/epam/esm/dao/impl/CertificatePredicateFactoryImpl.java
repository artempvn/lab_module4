package com.epam.esm.dao.impl;

import com.epam.esm.dao.PredicateFactory;
import com.epam.esm.dao.ReflectionService;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.CertificatesRequest;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CertificatePredicateFactoryImpl implements PredicateFactory {
  private static final List<String> FILTER_REQUEST_FIELDS = List.of("name", "description");
  private final ReflectionService reflectionService;

  public CertificatePredicateFactoryImpl(ReflectionService reflectionService) {
    this.reflectionService = reflectionService;
  }

  @Override
  public List<Predicate> buildPredicates(
      CriteriaBuilder builder, CertificatesRequest request, Root<Certificate> root) {
    List<Field> notNullFilterFields =
        reflectionService.takeNotNullFields(request, FILTER_REQUEST_FIELDS);
    return notNullFilterFields.stream()
        .map(takeFieldPredicateFunction(builder, request, root))
        .collect(Collectors.toList());
  }

  Function<Field, Predicate> takeFieldPredicateFunction(
      CriteriaBuilder builder, CertificatesRequest request, Root<Certificate> root) {
    return field -> {
      String fieldName = field.getName();
      String value = reflectionService.takeValueFromField(field, request).orElseThrow().toString();
      return builder.like(root.get(fieldName), String.format("%%%s%%", value));
    };
  }
}
