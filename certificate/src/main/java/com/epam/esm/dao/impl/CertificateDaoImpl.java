package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CriteriaHandler;
import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class CertificateDaoImpl implements CertificateDao {
  public static final String SQL_ADD_TAG =
      "INSERT INTO certificates_tags(tag_id,certificate_id) VALUES (:tag_id,:certificate_id)";
  public static final String SQL_REMOVE_TAG =
      "DELETE FROM certificates_tags WHERE tag_id=:tag_id AND certificate_id=:certificate_id ";

  private final CriteriaHandler criteriaHandler;
  private final PaginationHandler paginationHandler;
  private final EntityManager entityManager;

  public CertificateDaoImpl(
      CriteriaHandler criteriaHandler,
      PaginationHandler paginationHandler,
      EntityManager entityManager) {
    this.criteriaHandler = criteriaHandler;
    this.paginationHandler = paginationHandler;
    this.entityManager = entityManager;
  }

  @Override
  public CertificateDtoWithTags create(CertificateDtoWithTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    entityManager.persist(certificate);

    certificate.getTags().forEach(entityManager::merge);

    certificate.getTags().forEach(tag -> tag.withCertificate(certificate));

    return new CertificateDtoWithTags(certificate);
  }

  @Override
  public Optional<CertificateDtoWithTags> read(long id) {
    Optional<Certificate> certificate =
        Optional.ofNullable(entityManager.find(Certificate.class, id));
    return certificate.map(CertificateDtoWithTags::new);
  }

  @Override
  public PageData<CertificateDtoWithoutTags> readAll(
      CertificatesRequest request, PaginationParameter parameter) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Certificate> criteria = criteriaHandler.filterWithParameters(builder, request);

    TypedQuery<Certificate> typedQuery = entityManager.createQuery(criteria);
    int numberOfElements = typedQuery.getResultList().size();
    paginationHandler.setPageToQuery(typedQuery, parameter);

    long numberOfPages =
        paginationHandler.calculateNumberOfPages(numberOfElements, parameter.getSize());

    List<CertificateDtoWithoutTags> certificates =
        typedQuery.getResultList().stream()
            .map(CertificateDtoWithoutTags::new)
            .collect(Collectors.toList());

    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, certificates);
  }

  @Override
  public void update(CertificateDtoWithTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    Optional<Certificate> existingCertificate =
        Optional.ofNullable(entityManager.find(Certificate.class, certificate.getId()));
    existingCertificate.ifPresentOrElse(
        entityManager::detach,
        () -> {
          throw ResourceValidationException.validationWithCertificateId(certificate.getId()).get();
        });
    entityManager.merge(certificate);
  }

  @Override
  public void updatePresentedFields(CertificateDtoWithoutTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Certificate> criteria =
        criteriaHandler.updateWithNotNullFields(builder, certificate);
    int result = entityManager.createQuery(criteria).executeUpdate();
    if (result == 0) {
      throw ResourceValidationException.validationWithCertificateId(certificate.getId()).get();
    }
  }

  @Override
  public void delete(long id) {
    Optional<Certificate> certificate =
        Optional.ofNullable(entityManager.find(Certificate.class, id));
    certificate.ifPresentOrElse(
        entityManager::remove,
        () -> {
          throw ResourceValidationException.validationWithCertificateId(id).get();
        });
  }

  @Override
  public void addTag(long tagId, long certificateId) {
    Query query =
        entityManager
            .createNativeQuery(SQL_ADD_TAG)
            .setParameter("tag_id", tagId)
            .setParameter("certificate_id", certificateId);
    query.executeUpdate();
  }

  @Override
  public int removeTag(long tagId, long certificateId) {
    Query q =
        entityManager
            .createNativeQuery(SQL_REMOVE_TAG)
            .setParameter("tag_id", tagId)
            .setParameter("certificate_id", certificateId);
    return q.executeUpdate();
  }
}
