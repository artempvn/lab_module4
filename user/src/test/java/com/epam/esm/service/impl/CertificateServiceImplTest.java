package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateWithTagsDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CertificateServiceImplTest {
  CertificateDao certificateDao = mock(CertificateDao.class);
  TagService tagService = mock(TagService.class);
  CertificateServiceImpl certificateService =
      new CertificateServiceImpl(tagService, certificateDao);

  @Test
  void createCertificateDaoCreateInvocation() {
    Certificate certificate = givenCertificate();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(new CertificateWithTagsDto());

    verify(certificateDao).create(any());
  }

  @Test
  void createTagServiceNewInvocation() {
    Certificate certificate = givenCertificate();
    when(certificateDao.create(any())).thenReturn(certificate);
    TagDto tag = TagDto.builder().id(1L).build();
    when(tagService.create(any())).thenReturn(tag);
    CertificateWithTagsDto certificateDto =
        CertificateWithTagsDto.builder().tags(List.of(tag)).build();

    certificateService.create(certificateDto);

    verify(tagService).create(any());
  }

  Certificate givenCertificate() {
    Certificate certificate = new Certificate();
    certificate.setPreviousId(99L);
    certificate.setPrice(99.99);
    var tag = givenTag();
    certificate.setTags(List.of(tag));
    return certificate;
  }

  Tag givenTag() {
    Tag tag = new Tag();
    tag.setId(1L);
    tag.setName("tag name");
    return tag;
  }
}
