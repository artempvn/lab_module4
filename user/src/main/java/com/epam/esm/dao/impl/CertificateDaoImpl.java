package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.CertificateDtoWithTags;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CertificateDaoImpl implements CertificateDao {

  private final SessionFactory sessionFactory;

  public CertificateDaoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
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
}
