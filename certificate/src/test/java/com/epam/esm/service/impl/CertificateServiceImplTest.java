package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.CertificateDtoWithoutTags;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CertificateServiceImplTest {

  public static final long ID = 1L;

  CertificateDao certificateDao = mock(CertificateDao.class);
  TagService tagService = mock(TagService.class);
  CertificateServiceImpl certificateService =
      new CertificateServiceImpl(tagService, certificateDao);

  @Test
  void createCertificateDaoCreateInvocation() {
    CertificateDtoWithTags certificate = givenCertificate1();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(certificate);

    verify(certificateDao).create(certificate);
  }

  @Test
  void readExistedCertificateDaoReadInvocation() {
    CertificateDtoWithTags certificate = givenCertificate1();
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    certificateService.read(certificate.getId());

    verify(certificateDao).read(certificate.getId());
  }

  @Test
  void readNotExistedException() {
    when(certificateDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> certificateService.read(ID));
  }

  @Test
  void readAllCertificateDaoReadAllInvocation() {
    //    CertificateDtoWithoutTags certificate = new CertificateDtoWithoutTags();
    //    when(certificateDao.readAll(any(), any())).thenReturn(List.of(certificate));
    //
    //    certificateService.readAll(any(), any());
    //
    //    verify(certificateDao).readAll(any(), any());
  }

  @Test
  void updateCertificateDaoUpdatePatchInvocation() {
    CertificateDtoWithoutTags certificate = new CertificateDtoWithoutTags();

    certificateService.updatePresentedFields(certificate);

    verify(certificateDao).updatePresentedFields(any());
  }

  @Test
  void updatePut() {
    CertificateDtoWithTags certificate = givenCertificate1();

    certificateService.update(certificate);

    verify(certificateDao).update(any());
  }

  @Test
  void deleteCertificateDaoDeleteInvocation() {

    certificateService.delete(ID);

    verify(certificateDao).delete(ID);
  }

  private static CertificateDtoWithTags givenCertificate1() {
    return CertificateDtoWithTags.builder()
        .id(ID)
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }
}
