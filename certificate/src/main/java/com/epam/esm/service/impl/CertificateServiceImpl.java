package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.CertificateService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

  public static final int ONE_UPDATED_ROW = 1;
  private final CertificateDao certificateDao;
  private final TagDao tagDao;

  public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
    this.certificateDao = certificateDao;
    this.tagDao = tagDao;
  }

  @Override
  public Certificate create(Certificate certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    Certificate createdCertificate = certificateDao.create(certificate);
    // todo dto
    return createdCertificate;
  }

  @Override
  public Certificate read(long id) {
    Optional<Certificate> certificate = certificateDao.read(id);
    // todo dto
    return certificate.orElseThrow(ResourceNotFoundException.notFoundWithCertificateId(id));
  }

  @Override
  public List<Certificate> readAll() {
    // todo dto
    return certificateDao.readAll();
  }

  @Override // TODO refactoring
  public Certificate updatePatch(Certificate certificate) {
    //    LocalDateTime timeNow = LocalDateTime.now();
    //    certificate.setLastUpdateDate(timeNow);
    //    int numberOfUpdatedRows = certificateDao.updatePatch(certificate);
    //    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
    //      throw
    // ResourceValidationException.validationWithCertificateId(certificate.getId()).get();
    //    }
    return certificate;
  }

  @Override
  public Certificate updatePut(Certificate certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    try {
      certificateDao.update(certificate);
    } catch (ObjectOptimisticLockingFailureException ex) {
      throw ResourceValidationException.validationWithCertificateId(certificate.getId()).get();
    }
    // todo dto
    return certificate;
  }

  @Override
  public void delete(long id) {
    certificateDao.delete(id);
  }
}
