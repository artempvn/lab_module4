package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ResourceValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("certificate")
@AutoConfigureTestDatabase
@SpringBootTest
class CertificateDaoImplTest {

  public static final int NOT_EXISTED_CERTIFICATE_ID = 9999999;

  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;

  @AfterEach
  void setDown() {
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  void create() {
    CertificateWithTagsDto expectedCertificate = givenNewCertificateWithoutId();

    CertificateWithTagsDto actualCertificate = certificateDao.create(expectedCertificate);

    assertNotNull(actualCertificate.getId());
  }

  @Test
  void readExisted() {
    CertificateWithTagsDto certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    Optional<CertificateWithTagsDto> actualCertificate = certificateDao.read(id);

    assertTrue(actualCertificate.isPresent());
  }

  @Test
  void readNotExisted() {
    Optional<CertificateWithTagsDto> actualCertificate =
        certificateDao.read(NOT_EXISTED_CERTIFICATE_ID);

    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  void readAll() {
    CertificateWithTagsDto certificate1 = givenExistingCertificate1();
    CertificateWithTagsDto certificate2 = givenExistingCertificate2();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);
    List<CertificateWithTagsDto> expectedList = List.of(certificate1, certificate2);
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    parameter.setSize(10);

    PageData<CertificateWithoutTagsDto> actualPage =
        certificateDao.readAll(new CertificatesRequest(), parameter);
    assertEquals(expectedList.size(), actualPage.getContent().size());
  }

  @Test
  void update() {
    CertificateWithTagsDto certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();
    CertificateWithTagsDto expectedCertificate =
        CertificateWithTagsDto.builder().id(id).name("new name").build();

    certificateDao.update(expectedCertificate);

    CertificateWithTagsDto actualCertificate = certificateDao.read(id).get();
    assertEquals(expectedCertificate.getName(), actualCertificate.getName());
  }

  @Test
  void updateNotExisted() {
    CertificateWithTagsDto certificate1 = givenExistingCertificate1();
    certificate1.setId(1L);
    CertificateWithTagsDto expectedCertificate =
        CertificateWithTagsDto.builder().id(certificate1.getId()).name("new name").build();

    assertThrows(
        ResourceValidationException.class, () -> certificateDao.update(expectedCertificate));
  }

  @Test
  void updatePatch() {
    CertificateWithTagsDto certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    CertificateWithoutTagsDto updateCertificate = new CertificateWithoutTagsDto();
    updateCertificate.setId(id);
    updateCertificate.setName("new name");
    CertificateWithTagsDto expectedCertificate = givenExistingCertificate1();
    expectedCertificate.setId(id);
    expectedCertificate.setName(updateCertificate.getName());

    certificateDao.updatePresentedFields(updateCertificate);

    CertificateWithTagsDto actualCertificate = certificateDao.read(id).get();

    assertEquals(expectedCertificate, actualCertificate);
  }

  @Test
  void updatePatchNotExisted() {
    CertificateWithTagsDto certificate = givenExistingCertificate1();
    CertificateWithoutTagsDto updateCertificate = new CertificateWithoutTagsDto();
    updateCertificate.setId(certificate.getId());
    updateCertificate.setName("new name");

    assertThrows(
        ResourceValidationException.class,
        () -> certificateDao.updatePresentedFields(updateCertificate));
  }

  @Test
  void delete() {
    CertificateWithTagsDto certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    certificateDao.delete(id);

    Optional<CertificateWithTagsDto> actualCertificate = certificateDao.read(id);
    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  void deleteNotExisted() {

    assertThrows(
        ResourceValidationException.class, () -> certificateDao.delete(NOT_EXISTED_CERTIFICATE_ID));
  }

  @Test
  void addTag() {
    CertificateWithTagsDto certificate1 = givenExistingCertificate1();
    TagDto tag1 = givenExistingTag1();
    long certificateId = certificateDao.create(certificate1).getId();
    long tagId = tagDao.create(tag1).getId();
    List<Tag> expectedTags = List.of(new Tag(tag1));

    certificateDao.addTag(tagId, certificateId);

    List<TagDto> actualTags = certificateDao.read(certificateId).get().getTags();
    assertEquals(expectedTags.size(), actualTags.size());
  }

  @Test
  void removeTag() {
    CertificateWithTagsDto certificate2 = givenExistingCertificate2();
    TagDto tag1 = givenExistingTag1();
    TagDto tag2 = givenExistingTag2();
    long certificateId = certificateDao.create(certificate2).getId();
    long tagId1 = tagDao.create(tag1).getId();
    long tagId2 = tagDao.create(tag2).getId();
    certificateDao.addTag(tagId1, certificateId);
    certificateDao.addTag(tagId2, certificateId);
    tag1.setId(tagId1);
    List<TagDto> expectedTags = List.of(tag1);

    certificateDao.removeTag(tagId2, certificateId);

    List<TagDto> actualTags = certificateDao.read(certificateId).get().getTags();
    assertEquals(expectedTags, actualTags);
  }

  private static CertificateWithTagsDto givenExistingCertificate1() {
    return CertificateWithTagsDto.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }

  private static CertificateWithTagsDto givenExistingCertificate2() {
    return CertificateWithTagsDto.builder()
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static CertificateWithTagsDto givenNewCertificateWithoutId() {
    return CertificateWithTagsDto.builder()
        .name("third certificate")
        .description("third description")
        .price(3.33)
        .duration(15)
        .createDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static TagDto givenExistingTag1() {
    return TagDto.builder().name("first tag").build();
  }

  private static TagDto givenExistingTag2() {
    return TagDto.builder().name("second tag").build();
  }
}
