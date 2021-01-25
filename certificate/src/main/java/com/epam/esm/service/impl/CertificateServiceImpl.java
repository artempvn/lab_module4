package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDtoWithTags;
import com.epam.esm.entity.CertificateDtoWithoutTags;
import com.epam.esm.entity.CertificatesRequest;
import com.epam.esm.exception.ResourceException;
import com.epam.esm.service.CertificateService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
    Certificate createdCertificate = certificateDao.create(certificate);
    return new CertificateDtoWithTags(createdCertificate);
  }

  @Override
  public CertificateDtoWithTags read(long id) {
    Optional<Certificate> certificate = certificateDao.read(id);
    return new CertificateDtoWithTags(
        certificate.orElseThrow(ResourceException.notFoundWithCertificateId(id)));
  }

  @Override
  public List<CertificateDtoWithoutTags> readAll(CertificatesRequest request) {
    return certificateDao.readAll(request).stream()
        .map(CertificateDtoWithoutTags::new)
        .collect(Collectors.toList());
  }

  @Override
  public CertificateDtoWithoutTags updatePatch(CertificateDtoWithoutTags certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setLastUpdateDate(timeNow);
    certificateDao.updatePatch(certificate);
    return certificate;
  }

  @Override
  public CertificateDtoWithTags updatePut(CertificateDtoWithTags certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    try {
      certificateDao.update(certificate);
    } catch (ObjectOptimisticLockingFailureException ex) {
      throw ResourceException.validationWithCertificateId(certificate.getId()).get();
    }
    return certificate;
  }

  @Override
  public void delete(long id) {
    certificateDao.delete(id);
  }
}
