package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDtoFull;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CertificateServiceImplTest {
  CertificateDao certificateDao = mock(CertificateDao.class);
  TagService tagService = mock(TagService.class);
  CertificateServiceImpl certificateService =
      new CertificateServiceImpl(tagService, certificateDao);

  @Test
  void createCertificateDaoCreateInvocation() {
    CertificateDtoFull certificate = givenCertificate();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(certificate);

    verify(certificateDao).create(certificate);
  }

  @Test
  void createTagServiceNewInvocation() {
    CertificateDtoFull certificate = givenCertificate();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(certificate);

    verify(tagService).create(any());
  }

  CertificateDtoFull givenCertificate() {
    CertificateDtoFull certificate = new CertificateDtoFull();
    certificate.setPreviousId(99L);
    certificate.setPrice(99.99);
    var tag = givenTag();
    certificate.setTags(List.of(tag));
    return certificate;
  }

  TagDto givenTag() {
    TagDto tag = new TagDto();
    tag.setName("tag name");
    return tag;
  }
}
