package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.dao.SqlHandler;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
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
    session.beginTransaction();
    session.save(certificate);
    session.getTransaction().commit();
    session.close();
    return certificate;
  }

  @Override
  public Optional<Certificate> read(long id) {
    Session session = sessionFactory.getSessionFactory().openSession();
    Certificate certificate = session.get(Certificate.class, id);
    session.close();
    return Optional.ofNullable(certificate);
  }

    @Override
    public List<Certificate> readAll() {
      Session session = sessionFactory.getSessionFactory().openSession();
      List<Certificate> certificates = session.createQuery("from Certificate ").list();
      session.close();
      return certificates;
    }

  @Override
  public int update(Certificate certificate) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    String hql =
        "update  Certificate set name = :name , description= :description, duration = :duration,"
            + " price= :price, createDate = :creationDate, lastUpdateDate= :lastUpdateDate where id= :id ";
    Query q = session.createQuery(hql);
    q.setParameter("id", certificate.getId());
    q.setParameter(    "name", certificate.getName());
    q.setParameter("description", certificate.getDescription());
    q.setParameter("duration", certificate.getDuration());
    q.setParameter("price", certificate.getPrice());
    q.setParameter("creationDate", certificate.getCreateDate());
    q.setParameter("lastUpdateDate", certificate.getLastUpdateDate());
    int result = q.executeUpdate();
    session.getTransaction().commit();
    session.close();
    return result;
  }

  @Override
  public int delete(long id) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    String hql = "delete from Certificate where id = :id";
    Query q = session.createQuery(hql).setParameter("id", id);
    int result = q.executeUpdate();
    session.getTransaction().commit();
    session.close();
    return result;
  }

  @Override
  public void addTag(long tagId, long certificateId) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    Certificate certificate = session.get(Certificate.class, certificateId);
    Tag tag = session.get(Tag.class, tagId);
    certificate.getTags().add(tag);
    session.update(certificate);
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public int removeTag(long tagId, long certificateId) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    Certificate certificate = session.get(Certificate.class, certificateId);
    Tag tag = session.get(Tag.class, tagId);
    certificate.getTags().remove(tag);
    session.update(certificate);
    session.getTransaction().commit();
    session.close();
    return 1;//TODO set to void later
  }

  @Override
  public List<Tag> readCertificateTags(long certificateId) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    Certificate certificate = session.get(Certificate.class, certificateId);
    return certificate.getTags();
  }

  @Override
  public void deleteCertificateTagsByTagId(long tagId) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    String sql = "DELETE FROM certificates_tags WHERE tag_id= :id";
    Query q = session.createNativeQuery(sql).setParameter("id", tagId);
    q.executeUpdate();
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void deleteCertificateTagsByCertificateId(long certificateId) {
    Session session = sessionFactory.getSessionFactory().openSession();
    session.beginTransaction();
    Certificate certificate = session.get(Certificate.class, certificateId);
    certificate.setTags(Collections.emptyList());
    session.update(certificate);
    session.getTransaction().commit();
    session.close();
  }

   @Override
    public int updatePatch(Certificate certificate) {
      Session session = sessionFactory.getSessionFactory().openSession();
     session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaUpdate<Certificate> criteria = builder.createCriteriaUpdate(Certificate.class);
      Root<Certificate> root = criteria.from(Certificate.class);
      criteria.set(root.get("duration"), 30);
      criteria.where(builder.equal(root.get("id"), certificate.getId()));
      int result=session.createQuery(criteria).executeUpdate();
     session.getTransaction().commit();
      session.close();
      return result;
    }
}
