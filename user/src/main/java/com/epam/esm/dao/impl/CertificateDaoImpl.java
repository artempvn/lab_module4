package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
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
  public Certificate create(Certificate certificate) {
    entityManager.persist(certificate);

    certificate.getTags().forEach(entityManager::merge);
    certificate.getTags().forEach(tag -> tag.withCertificate(certificate));

    return certificate;
  }
}
