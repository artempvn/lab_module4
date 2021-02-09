package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateWithTagsDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

  private final TagService tagService;
  private final CertificateDao certificateDao;

  public CertificateServiceImpl(TagService tagService, CertificateDao certificateDao) {
    this.tagService = tagService;
    this.certificateDao = certificateDao;
  }

  @Override
  public CertificateWithTagsDto create(CertificateWithTagsDto certificate) {
    certificate.setTags(
        certificate.getTags().stream().map(tagService::create).collect(Collectors.toList()));
    Certificate certificateEntity = new Certificate(certificate);
    return new CertificateWithTagsDto(certificateDao.create(certificateEntity));
  }
}
