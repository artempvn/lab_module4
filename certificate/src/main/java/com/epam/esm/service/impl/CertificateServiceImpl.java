package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CertificateServiceImpl implements CertificateService {

  private final TagService tagService;
  private final CertificateDao certificateDao;

  public CertificateServiceImpl(TagService tagService, CertificateDao certificateDao) {
    this.tagService = tagService;
    this.certificateDao = certificateDao;
  }

  @Override
  public CertificateWithTagsDto create(CertificateWithTagsDto certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    certificate.setTags(
        certificate.getTags().stream().map(tagService::create).collect(Collectors.toList()));
    return certificateDao.create(certificate);
  }

  @Override
  public CertificateWithTagsDto read(long id) {
    Optional<CertificateWithTagsDto> certificate = certificateDao.read(id);
    return certificate.orElseThrow(ResourceNotFoundException.notFoundWithCertificateId(id));
  }

  @Override
  public PageData<CertificateWithoutTagsDto> readAll(
      CertificatesRequest request, PaginationParameter parameter) {
    return certificateDao.readAll(request, parameter);
  }

  @Override
  public CertificateWithoutTagsDto updatePresentedFields(CertificateWithoutTagsDto certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setLastUpdateDate(timeNow);
    certificateDao.updatePresentedFields(certificate);
    return certificate;
  }

  @Override
  public CertificateWithTagsDto update(CertificateWithTagsDto certificate) {
    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    certificate.setTags(
        certificate.getTags().stream().map(tagService::create).collect(Collectors.toList()));
    certificateDao.update(certificate);
    return certificate;
  }

  @Override
  public void delete(long id) {
    certificateDao.delete(id);
  }
}
