package com.epam.esm.dao;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.CertificatesRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface PredicateFactory {

  List<Predicate> buildPredicates(
      CriteriaBuilder builder, CertificatesRequest request, Root<Certificate> root);
}
