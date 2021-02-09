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
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CertificateServiceImplTest {

  public static final long ID = 1L;

  CertificateDao certificateDao = mock(CertificateDao.class);
  TagService tagService = mock(TagService.class);
  CertificateServiceImpl certificateService =
      new CertificateServiceImpl(tagService, certificateDao);

  @Test
  void createCertificateDaoCreateInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(new CertificateWithTagsDto());

    verify(certificateDao).create(any());
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
    Certificate certificate = new Certificate();
    PageData<Certificate> page = new PageData<>(1, 1, 1, List.of(certificate));
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    parameter.setSize(1);
    when(certificateDao.readAll(any(), any())).thenReturn(page);

    certificateService.readAll(new CertificatesRequest(), parameter);

    verify(certificateDao).readAll(any(), any());
  }

  @Test
  void updateCertificateDaoUpdatePatchInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));
    CertificateWithoutTagsDto certificateInput = new CertificateWithoutTagsDto();
    certificateInput.setId(ID);

    certificateService.updatePresentedFields(certificateInput);

    verify(certificateDao).updatePresentedFields(any());
  }

  @Test
  void updateCertificatePresentedFieldsNotExisted() {
    CertificateWithoutTagsDto certificateInput = new CertificateWithoutTagsDto();
    certificateInput.setId(ID);
    when(certificateDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(
        ResourceValidationException.class,
        () -> certificateService.updatePresentedFields(certificateInput));
  }

  @Test
  void updatePut() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));
    CertificateWithTagsDto certificateInput = new CertificateWithTagsDto();
    certificateInput.setId(ID);

    certificateService.update(certificateInput);

    verify(certificateDao).update(any());
  }

  @Test
  void updateCertificateNotExisted() {
    CertificateWithTagsDto certificateInput = new CertificateWithTagsDto();
    certificateInput.setId(ID);
    when(certificateDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(
        ResourceValidationException.class, () -> certificateService.update(certificateInput));
  }

  @Test
  void deleteCertificateDaoDeleteInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    certificateService.delete(ID);

    verify(certificateDao).delete(ID);
  }

  @Test
  void deleteCertificateNotExisted() {
    CertificateWithTagsDto certificateInput = new CertificateWithTagsDto();
    certificateInput.setId(ID);
    when(certificateDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceValidationException.class, () -> certificateService.delete(ID));
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
