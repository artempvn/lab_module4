package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
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
  public static final int NO_UPDATED_ROWS = 0;
  public static final int ONE_UPDATED_ROW = 1;

  TagDao tagDao = mock(TagDao.class);
  CertificateDao certificateDao = mock(CertificateDao.class);
  CertificateServiceImpl certificateService = new CertificateServiceImpl(certificateDao, tagDao);

  @Test
  void createCertificateDaoCreateInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(certificate);

    verify(certificateDao).create(certificate);
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
    when(certificateDao.readAll()).thenReturn(List.of(certificate));

    certificateService.readAll();

    verify(certificateDao).readAll();
  }

  //  @Test
  //  void updateCertificateDaoUpdatePatchInvocation() {
  //    CertificatePatch certificate = new CertificatePatch();
  //    when(certificateDao.updatePatch(any())).thenReturn(ONE_UPDATED_ROW);
  //
  //    certificateService.updatePatch(certificate);
  //
  //    verify(certificateDao).updatePatch(any());
  //  }
  //
  //  @Test
  //  void updateCertificateDaoUpdatePatchException() {
  //    CertificatePatch certificate = new CertificatePatch();
  //    when(certificateDao.updatePatch(any())).thenReturn(NO_UPDATED_ROWS);
  //
  //    assertThrows(
  //        ResourceValidationException.class, () -> certificateService.updatePatch(certificate));
  //  }

  @Test
  void updatePut() {
    Certificate certificate = givenCertificate1();

    certificateService.updatePut(certificate);

    verify(certificateDao).update(any());
  }

  @Test
  void updatePutNotExistedCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    Certificate certificate = givenCertificate1();
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

  private static Certificate givenCertificate2() {
    return Certificate.builder().id(ID).duration(20).tags(List.of(new Tag())).build();
  }

  private static Certificate givenCertificate3() {
    return Certificate.builder().id(ID).duration(20).build();
  }

  private static Tag givenTag() {
    return Tag.builder().id(ID).build();
  }
}
