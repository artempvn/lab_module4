package com.epam.esm.dao;

import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.entity.Certificate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;

/** The interface Criteria handler. */
public interface CriteriaHandler {

  /**
   * Update with not null fields criteria update.
   *
   * @param builder the builder
   * @param certificate the certificate with fields for updating existing certificate
   * @return the criteria update for certificate
   */
  CriteriaUpdate<Certificate> updateWithNotNullFields(
      CriteriaBuilder builder, Certificate certificate);

  /**
   * Filter with parameters criteria query.
   *
   * @param builder the builder
   * @param request the request contains sorting and filtering staff
   * @return the criteria query
   */
  CriteriaQuery<Certificate> filterWithParameters(
      CriteriaBuilder builder, CertificatesRequest request);
}
