package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {
  private final HibernateSessionFactoryUtil sessionFactory;

  public CertificateDaoImpl(HibernateSessionFactoryUtil sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Certificate create(Certificate certificate) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.save(certificate);
    session.close();
    return certificate;
  }

  @Override
  public Optional<Certificate> read(long id) {
    Session session = sessionFactory.getSessionFactory().openSession();
    Optional<Certificate> certificate = Optional.ofNullable(session.get(Certificate.class, id));
    session.close();
    return certificate;
  }

  @Override
  public List<Certificate> readAll() {
    Session session = sessionFactory.getSessionFactory().openSession();
    List<Certificate> certificates = session.createQuery("from Certificate ").list();
    session.close();
    return certificates;
  }

  @Override
  public void update(Certificate certificate) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    session.update(certificate);
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void delete(long id) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    Optional<Certificate> certificate = Optional.ofNullable(session.get(Certificate.class, id));
    certificate.ifPresentOrElse(
        session::delete,
        () -> {
          throw ResourceValidationException.validationWithCertificateId(id).get();
        });
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void addTag(long tagId, long certificateId) {
    Session session = sessionFactory.getSessionFactory().openSession();
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
    session.close();
  }

  @Override
  public int removeTag(long tagId, long certificateId) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    String sql =
        "DELETE FROM certificates_tags WHERE tag_id=:tag_id AND certificate_id=:certificate_id ";
    Query q =
        session
            .createNativeQuery(sql)
            .setParameter("tag_id", tagId)
            .setParameter("certificate_id", certificateId);
    int result = q.executeUpdate();
    session.getTransaction().commit();
    session.close();
    return result;
  }

  @Override // TODO refactor
  public int updatePatch(Certificate certificate) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<Certificate> criteria = builder.createCriteriaUpdate(Certificate.class);
    Root<Certificate> root = criteria.from(Certificate.class);
    criteria.set(root.get("duration"), 30);
    criteria.where(builder.equal(root.get("id"), certificate.getId()));
    int result = session.createQuery(criteria).executeUpdate();
    session.getTransaction().commit();
    session.close();
    return result;
  }
}
