package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import org.junit.jupiter.api.Test;

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
  void createNoTagsTagDaoReadInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(certificate);

    verify(tagDao, never()).read(any());
  }

  @Test
  void createNoTagsTagDaoCreateInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(certificate);

    verify(tagDao, never()).create(any());
  }

  @Test
  void createNoTagsCertificateDaoAddTagInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.create(any())).thenReturn(certificate);

    certificateService.create(certificate);

    verify(certificateDao, never()).addTag(anyLong(), anyLong());
  }

  @Test
  void createWithTagsExistedTagDaoReadInvocation() {
    Certificate certificate = givenCertificate2();
    Certificate certificateOutput = givenCertificate2();
    certificateOutput.setTags(null);
    Tag tag = givenTag();
    when(certificateDao.create(any())).thenReturn(certificateOutput);
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    certificateService.create(certificate);

    verify(tagDao).read(any());
  }

  @Test
  void createWithTagsExistedTagDaoCreateInvocation() {
    Certificate certificate = givenCertificate2();
    Certificate certificateOutput = givenCertificate2();
    certificateOutput.setTags(null);
    Tag tag = givenTag();
    when(certificateDao.create(any())).thenReturn(certificateOutput);
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    certificateService.create(certificate);

    verify(tagDao, never()).create(any());
  }

  @Test
  void createWithTagsExistedCertificateDaoAddTagInvocation() {
    Certificate certificate = givenCertificate2();
    Certificate certificateOutput = givenCertificate2();
    certificateOutput.setTags(null);
    Tag tag = givenTag();
    when(certificateDao.create(any())).thenReturn(certificateOutput);
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    certificateService.create(certificate);

    verify(certificateDao).addTag(anyLong(), anyLong());
  }

  @Test
  void createWithTagsNotExistedTagDaoReadInvocation() {
    Certificate certificate = givenCertificate2();
    Certificate certificateOutput = givenCertificate2();
    certificateOutput.setTags(null);
    Tag tag = givenTag();
    when(certificateDao.create(any())).thenReturn(certificateOutput);
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(tag);

    certificateService.create(certificate);

    verify(tagDao).read(any());
  }

  @Test
  void createWithTagsNotExistedTagDaoCreateInvocation() {
    Certificate certificate = givenCertificate2();
    Certificate certificateOutput = givenCertificate2();
    certificateOutput.setTags(null);
    Tag tag = givenTag();
    when(certificateDao.create(any())).thenReturn(certificateOutput);
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(tag);

    certificateService.create(certificate);

    verify(tagDao).create(any());
  }

  @Test
  void createWithTagsNotExistedCertificateDaoAddTagInvocation() {
    Certificate certificate = givenCertificate2();
    Certificate certificateOutput = givenCertificate2();
    certificateOutput.setTags(null);
    Tag tag = givenTag();
    when(certificateDao.create(any())).thenReturn(certificateOutput);
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(tag);

    certificateService.create(certificate);

    verify(certificateDao).addTag(anyLong(), anyLong());
  }

  @Test
  void readExistedCertificateDaoReadInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    certificateService.read(certificate.getId());

    verify(certificateDao).read(certificate.getId());
  }

  @Test
  void readExistedCertificateDaoReadCertificateTagsInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    certificateService.read(certificate.getId());

    verify(certificateDao).readCertificateTags(certificate.getId());
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
    when(certificateDao.update(any())).thenReturn(ONE_UPDATED_ROW);

    certificateService.updatePut(certificate);

    verify(certificateDao).update(any());
  }

  @Test
  void updatePutNotExistedCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.update(any())).thenReturn(NO_UPDATED_ROWS);

    assertThrows(
        ResourceValidationException.class, () -> certificateService.updatePut(certificate));
  }

  @Test
  void updatePutExistedCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    Certificate certificate = givenCertificate1();
    when(certificateDao.update(any())).thenReturn(ONE_UPDATED_ROW);

    certificateService.updatePut(certificate);

    verify(certificateDao).deleteCertificateTagsByCertificateId(anyLong());
  }

  @Test
  void deleteCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    when(certificateDao.delete(anyLong())).thenReturn(ONE_UPDATED_ROW);

    certificateService.delete(ID);

    verify(certificateDao).deleteCertificateTagsByCertificateId(ID);
  }

  @Test
  void deleteCertificateDaoDeleteInvocation() {
    when(certificateDao.delete(anyLong())).thenReturn(ONE_UPDATED_ROW);

    certificateService.delete(ID);

    verify(certificateDao).delete(ID);
  }

  @Test
  void deleteCertificateDaoDeleteException() {
    when(certificateDao.delete(anyLong())).thenReturn(NO_UPDATED_ROWS);

    assertThrows(ResourceValidationException.class, () -> certificateService.delete(ID));
  }

  @Test
  void addTagsToDbNoTagsTagDaoRead() {
    Certificate certificate = givenCertificate1();

    certificateService.addTagsToDb(certificate);

    verify(tagDao, never()).read(any());
  }

  @Test
  void addTagsToDbNoTagsTagDaoCreate() {
    Certificate certificate = givenCertificate1();

    certificateService.addTagsToDb(certificate);

    verify(tagDao, never()).create(any());
  }

  @Test
  void addTagsToDbNoTagsCertificateDaoAddTag() {
    Certificate certificate = givenCertificate1();

    certificateService.addTagsToDb(certificate);

    verify(certificateDao, never()).addTag(anyLong(), anyLong());
  }

  @Test
  void addTagsToDbWithTagTagDaoRead() {
    Certificate certificate = givenCertificate2();
    Tag tag = givenTag();
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    certificateService.addTagsToDb(certificate);

    verify(tagDao).read(any());
  }

  @Test
  void addTagsToDbWithExistedTagTagDaoCreate() {
    Certificate certificate = givenCertificate1();
    Tag tag = givenTag();
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    certificateService.addTagsToDb(certificate);

    verify(tagDao, never()).create(any());
  }

  @Test
  void addTagsToDbWithTagCertificateDaoAddTag() {
    Certificate certificate = givenCertificate2();
    Tag tag = givenTag();
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    certificateService.addTagsToDb(certificate);

    verify(certificateDao).addTag(anyLong(), anyLong());
  }

  @Test
  void addTagsToDbWithUnexistedTagTagDaoCreate() {
    Certificate certificate = givenCertificate2();
    Tag tag = givenTag();
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(tag);

    certificateService.addTagsToDb(certificate);

    verify(tagDao).create(any());
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
