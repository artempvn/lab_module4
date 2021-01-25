package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDtoWithTags;
import com.epam.esm.entity.CertificateDtoWithoutTags;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CertificateServiceImplTest {

  public static final long ID = 1L;

  TagDao tagDao = mock(TagDao.class);
  CertificateDao certificateDao = mock(CertificateDao.class);
  CertificateServiceImpl certificateService = new CertificateServiceImpl(certificateDao, tagDao);

  @Test
  void createCertificateDaoCreateInvocation() {
    Certificate certificate = givenCertificate1();
    CertificateDtoWithTags dto = new CertificateDtoWithTags(certificate);
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(dto);

    verify(certificateDao).create(dto);
  }

  @Test
  void readExistedCertificateDaoReadInvocation() {
    Certificate certificate = givenCertificate1();
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
    Certificate certificate = givenCertificate1();
    when(certificateDao.readAll(any())).thenReturn(List.of(certificate));

    certificateService.readAll(any());

    verify(certificateDao).readAll(any());
  }

  @Test
  void updateCertificateDaoUpdatePatchInvocation() {
    CertificateDtoWithoutTags certificate = new CertificateDtoWithoutTags();

    certificateService.updatePatch(certificate);

    verify(certificateDao).updatePatch(any());
  }

  @Test
  void updatePut() {
    CertificateDtoWithTags certificate = new CertificateDtoWithTags(givenCertificate1());

    certificateService.updatePut(certificate);

    verify(certificateDao).update(any());
  }

  @Test
  void updatePutNotExistedCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    CertificateDtoWithTags certificate = new CertificateDtoWithTags(givenCertificate1());
    doThrow(ObjectOptimisticLockingFailureException.class).when(certificateDao).update(any());

    assertThrows(
        ResourceValidationException.class, () -> certificateService.updatePut(certificate));
  }

  @Test
  void deleteCertificateDaoDeleteInvocation() {

    certificateService.delete(ID);

    verify(certificateDao).delete(ID);
  }

  private static Certificate givenCertificate1() {
    return Certificate.builder()
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
