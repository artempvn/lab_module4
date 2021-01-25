package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CriteriaHandler;
import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDtoWithTags;
import com.epam.esm.entity.CertificateDtoWithoutTags;
import com.epam.esm.entity.CertificatesRequest;
import com.epam.esm.exception.ResourceException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {
  private final HibernateSessionFactoryUtil sessionFactory;
  private final CriteriaHandler criteriaHandler;

  public CertificateDaoImpl(
      HibernateSessionFactoryUtil sessionFactory, CriteriaHandler criteriaHandler) {
    this.sessionFactory = sessionFactory;
    this.criteriaHandler = criteriaHandler;
  }

  @Override
  public Certificate create(CertificateDtoWithTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      session.save(certificate);
    }
    return certificate;
  }

  @Override
  public Optional<Certificate> read(long id) {
    Optional<Certificate> certificate = Optional.empty();
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      certificate = Optional.ofNullable(session.get(Certificate.class, id));
    }
    return certificate;
  }

  @Override
  public List<Certificate> readAll(CertificatesRequest request) {
    List<Certificate> certificates = Collections.emptyList();
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Certificate> criteria = criteriaHandler.filterWithParameters(builder, request);
      certificates = session.createQuery(criteria).list();
    }
    return certificates;
  }

  @Override
  public void update(CertificateDtoWithTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      session.update(certificate);
      session.getTransaction().commit();
    }
  }

  @Override
  public void updatePatch(CertificateDtoWithoutTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaUpdate<Certificate> criteria =
          criteriaHandler.updateWithNotNullFields(builder, certificate);
      int result = session.createQuery(criteria).executeUpdate();
      if (result == 0) {
        throw ResourceException.validationWithCertificateId(certificate.getId()).get();
      }
      session.getTransaction().commit();
    }
  }

  @Override
  public void delete(long id) {
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      Optional<Certificate> certificate = Optional.ofNullable(session.get(Certificate.class, id));
      certificate.ifPresentOrElse(
          session::delete,
          () -> {
            throw ResourceException.validationWithCertificateId(id).get();
          });
      session.getTransaction().commit();
    }
  }

  @Override
  public void addTag(long tagId, long certificateId) {
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      String sql =
          "INSERT INTO certificates_tags(tag_id,certificate_id) VALUES (:tag_id,:certificate_id)";
      Query q =
          session
              .createNativeQuery(sql)
              .setParameter("tag_id", tagId)
              .setParameter("certificate_id", certificateId);
      q.executeUpdate();
      session.getTransaction().commit();
    }
  }

  @Override
  public int removeTag(long tagId, long certificateId) {
    int result = 0;
    try (Session session = sessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      String sql =
          "DELETE FROM certificates_tags WHERE tag_id=:tag_id AND certificate_id=:certificate_id ";
      Query q =
          session
              .createNativeQuery(sql)
              .setParameter("tag_id", tagId)
              .setParameter("certificate_id", certificateId);
      result = q.executeUpdate();
      session.getTransaction().commit();
    }
    return result;
  }
}
