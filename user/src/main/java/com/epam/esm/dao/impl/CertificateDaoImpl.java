package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.CertificateDtoWithTags;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class CertificateDaoImpl implements CertificateDao {

  private final EntityManager entityManager;

  public CertificateDaoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public CertificateDtoWithTags create(CertificateDtoWithTags certificateDto) {
    Certificate certificate = new Certificate(certificateDto);
    Session session = entityManager.unwrap(Session.class);
    session.save(certificate);

    certificate.getTags().forEach(session::merge);
    certificate.getTags().forEach(tag -> tag.withCertificate(certificate));

    return new CertificateDtoWithTags(certificate);
  }
}
