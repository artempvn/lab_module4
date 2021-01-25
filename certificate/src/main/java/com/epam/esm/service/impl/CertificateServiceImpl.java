package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.CertificateDtoWithTags;
import com.epam.esm.entity.CertificateDtoWithoutTags;
import com.epam.esm.entity.CertificatesRequest;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.CertificateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CertificateServiceImpl implements CertificateService {

  private final CertificateDao certificateDao;
  private final TagDao tagDao;

  public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
    this.certificateDao = certificateDao;
    this.tagDao = tagDao;
  }

  @Override
  public CertificateDtoWithTags create(CertificateDtoWithTags certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    CertificateDtoWithTags createdCertificate = certificateDao.create(certificate);
    return createdCertificate;
  }

  @Override
  public CertificateDtoWithTags read(long id) {
    Optional<CertificateDtoWithTags> certificate = certificateDao.read(id);
    return certificate.orElseThrow(ResourceNotFoundException.notFoundWithCertificateId(id));
  }

  @Override
  public List<CertificateDtoWithoutTags> readAll(CertificatesRequest request) {
    return certificateDao.readAll(request);
  }

  @Override
  public CertificateDtoWithoutTags updatePresentedFields(CertificateDtoWithoutTags certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setLastUpdateDate(timeNow);
    certificateDao.updatePresentedFields(certificate);
    return certificate;
  }

  @Override
  public CertificateDtoWithTags update(CertificateDtoWithTags certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    certificateDao.update(certificate);
    return certificate;
  }

  @Override
  public void delete(long id) {
    certificateDao.delete(id);
  }
}
