package com.epam.esm.service.impl;



import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificatePatch;
import com.epam.esm.entity.CertificatesRequest;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.CertificateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Certificate create(Certificate certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    Certificate createdCertificate = certificateDao.create(certificate);
    createdCertificate.setTags(certificate.getTags());
    addTagsToDb(createdCertificate);
    return createdCertificate;
  }

  @Override
  public Certificate read(long id) {
    Optional<Certificate> certificate = certificateDao.read(id);
    certificate.ifPresent(
        actualCertificate -> actualCertificate.setTags(certificateDao.readCertificateTags(id)));
    return certificate.orElseThrow(ResourceNotFoundException.notFoundWithCertificateId(id));
  }

  @Override
  public List<Certificate> readAll(CertificatesRequest parameter) {
    List<Certificate> certificates = certificateDao.readAll();
    for (Certificate certificate : certificates) {
      certificate.setTags(certificateDao.readCertificateTags(certificate.getId()));
    }
    return certificates;
  }

  @Override //TODO refactoring
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public CertificatePatch updatePatch(CertificatePatch certificate) {
//    LocalDateTime timeNow = LocalDateTime.now();
//    certificate.setLastUpdateDate(timeNow);
//    int numberOfUpdatedRows = certificateDao.updatePatch(certificate);
//    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
//      throw ResourceValidationException.validationWithCertificateId(certificate.getId()).get();
//    }
    return certificate;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Certificate updatePut(Certificate certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    int numberOfUpdatedRows = certificateDao.update(certificate);
    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
      throw ResourceValidationException.validationWithCertificateId(certificate.getId()).get();
    }
    certificateDao.deleteCertificateTagsByCertificateId(certificate.getId());
    addTagsToDb(certificate);
    return certificate;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void delete(long id) {
    certificateDao.deleteCertificateTagsByCertificateId(id);
    int numberOfUpdatedRows = certificateDao.delete(id);
    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
      throw ResourceValidationException.validationWithCertificateId(id).get();
    }
  }

  void addTagsToDb(Certificate certificate) {
    List<Tag> tags = certificate.getTags();
    if (tags != null) {
      for (Tag tag : tags) {
        Optional<Tag> existedTag = tagDao.read(tag.getName());
        long tagId = existedTag.map(Tag::getId).orElseGet(() -> tagDao.create(tag).getId());
        certificateDao.addTag(tagId, certificate.getId());
      }
    }
  }
}
