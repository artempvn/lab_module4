package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateWithTagsDto;
import com.epam.esm.dto.CertificateWithoutTagsDto;
import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    Certificate certificateEntity = new Certificate(certificate);
    return new CertificateWithTagsDto(certificateDao.create(certificateEntity));
  }

  @Override
  public CertificateWithTagsDto read(long id) {
    Optional<Certificate> certificate = certificateDao.read(id);
    return certificate
        .map(CertificateWithTagsDto::new)
        .orElseThrow(ResourceNotFoundException.notFoundWithCertificateId(id));
  }

  @Override
  public PageData<CertificateWithoutTagsDto> readAll(
      CertificatesRequest request, PaginationParameter parameter) {
    PageData<Certificate> certificatePageData = certificateDao.readAll(request, parameter);
    long numberOfElements = certificatePageData.getNumberOfElements();
    long numberOfPages = certificatePageData.getNumberOfPages();
    List<CertificateWithoutTagsDto> certificates =
        certificatePageData.getContent().stream()
            .map(CertificateWithoutTagsDto::new)
            .collect(Collectors.toList());
    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, certificates);
  }

  @Override
  public CertificateWithoutTagsDto updatePresentedFields(CertificateWithoutTagsDto certificate) {
    long id = certificate.getId();
    Optional<Certificate> existedCertificate = certificateDao.read(id);
    existedCertificate.orElseThrow(ResourceValidationException.validationWithCertificateId(id));

    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setLastUpdateDate(timeNow);
    certificateDao.updatePresentedFields(new Certificate(certificate));
    return certificate;
  }

  @Override
  public CertificateWithTagsDto update(CertificateWithTagsDto certificate) {
    long id = certificate.getId();
    Optional<Certificate> existedCertificate = certificateDao.read(id);
    existedCertificate.orElseThrow(ResourceValidationException.validationWithCertificateId(id));

    LocalDateTime timeNow = LocalDateTime.now();
    certificate.setCreateDate(timeNow);
    certificate.setLastUpdateDate(timeNow);
    certificate.setTags(
        certificate.getTags().stream().map(tagService::create).collect(Collectors.toList()));
    certificateDao.update(new Certificate(certificate));
    return certificate;
  }

  @Override
  public void delete(long id) {
    Optional<Certificate> certificate = certificateDao.read(id);
    certificate.orElseThrow(ResourceValidationException.validationWithCertificateId(id));
    certificateDao.delete(id);
  }
}
