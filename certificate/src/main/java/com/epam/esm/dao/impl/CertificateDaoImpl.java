package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CriteriaHandler;
import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
  private final SessionFactory sessionFactory;
  private final CriteriaHandler criteriaHandler;
  private final PaginationHandler paginationHandler;

  public CertificateDaoImpl(
      SessionFactory sessionFactory,
      CriteriaHandler criteriaHandler,
      PaginationHandler paginationHandler) {
    this.sessionFactory = sessionFactory;
    this.criteriaHandler = criteriaHandler;
    this.paginationHandler = paginationHandler;
  }

  @Override
  public CertificateDtoWithTags create(CertificateDtoWithTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    Session session = sessionFactory.getCurrentSession();
    session.save(certificate);

    certificate.getTags().forEach(session::merge);

    certificate.getTags().forEach(tag -> tag.withCertificate(certificate));

    return new CertificateDtoWithTags(certificate);
  }

  @Override
  public Optional<CertificateDtoWithTags> read(long id) {
    Session session = sessionFactory.getCurrentSession();
    Optional<Certificate> certificate = Optional.ofNullable(session.get(Certificate.class, id));
    return certificate.map(CertificateDtoWithTags::new);
  }

  @Override
  public PageData<CertificateDtoWithoutTags> readAll(
      CertificatesRequest request, PaginationParameter parameter) {
    Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Certificate> criteria = criteriaHandler.filterWithParameters(builder, request);

    TypedQuery<Certificate> typedQuery = session.createQuery(criteria);
    paginationHandler.setPageToQuery(typedQuery, parameter);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
    countQuery.select(builder.count(countQuery.from(Certificate.class)));
    Long numberOfElements = session.createQuery(countQuery).getSingleResult();
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
    Session session = sessionFactory.getCurrentSession();
    Optional<Certificate> existingCertificate =
        Optional.ofNullable(session.get(Certificate.class, certificate.getId()));
    existingCertificate.ifPresentOrElse(
        session::detach,
        () -> {
          throw ResourceValidationException.validationWithCertificateId(certificate.getId()).get();
        });
    session.update(certificate);
  }

  @Override
  public void updatePresentedFields(CertificateDtoWithoutTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<Certificate> criteria =
        criteriaHandler.updateWithNotNullFields(builder, certificate);
    int result = session.createQuery(criteria).executeUpdate();
    if (result == 0) {
      throw ResourceValidationException.validationWithCertificateId(certificate.getId()).get();
    }
  }

  @Override
  public void delete(long id) {
    Session session = sessionFactory.getCurrentSession();
    Optional<Certificate> certificate = Optional.ofNullable(session.get(Certificate.class, id));
    certificate.ifPresentOrElse(
        session::delete,
        () -> {
          throw ResourceValidationException.validationWithCertificateId(id).get();
        });
  }

  @Override
  public void addTag(long tagId, long certificateId) {
    Session session = sessionFactory.getCurrentSession();
    Query q =
        session
            .createNativeQuery(SQL_ADD_TAG)
            .setParameter("tag_id", tagId)
            .setParameter("certificate_id", certificateId);
    q.executeUpdate();
  }

  @Override
  public int removeTag(long tagId, long certificateId) {
    Session session = sessionFactory.getCurrentSession();
    Query q =
        session
            .createNativeQuery(SQL_REMOVE_TAG)
            .setParameter("tag_id", tagId)
            .setParameter("certificate_id", certificateId);
    int result = q.executeUpdate();
    return result;
  }
}
