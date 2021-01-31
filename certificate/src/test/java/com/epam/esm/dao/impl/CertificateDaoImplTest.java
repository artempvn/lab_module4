package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.*;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
  @Autowired SessionFactory sessionFactory;

  @AfterEach
  void setDown() {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
      session.createNativeQuery(sql).executeUpdate();
      session.getTransaction().commit();
    }
  }

  @Test
  void create() {
    CertificateDtoWithTags expectedCertificate = givenNewCertificateWithoutId();

    CertificateDtoWithTags actualCertificate = certificateDao.create(expectedCertificate);

    assertNotNull(actualCertificate.getId());
  }

  @Test
  void readExisted() {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    Optional<CertificateDtoWithTags> actualCertificate = certificateDao.read(id);

    assertTrue(actualCertificate.isPresent());
  }

  @Test
  void readNotExisted() {
    Optional<CertificateDtoWithTags> actualCertificate =
        certificateDao.read(NOT_EXISTED_CERTIFICATE_ID);

    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  void readAll() {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    CertificateDtoWithTags certificate2 = givenExistingCertificate2();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);
    List<Certificate> expectedList =
        List.of(new Certificate(certificate1), new Certificate(certificate2));

    List<CertificateDtoWithoutTags> actualList = certificateDao.readAll(new CertificatesRequest(),new PaginationParameter());
    assertEquals(expectedList.size(), actualList.size());
  }

  @Test
  void update() {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();
    CertificateDtoWithTags expectedCertificate =
        CertificateDtoWithTags.builder().id(id).name("new name").build();

    certificateDao.update(expectedCertificate);

    CertificateDtoWithTags actualCertificate = certificateDao.read(id).get();
    assertEquals(expectedCertificate.getName(), actualCertificate.getName());
  }

  @Test
  void updateNotExisted() {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    CertificateDtoWithTags expectedCertificate =
        CertificateDtoWithTags.builder().id(certificate1.getId()).name("new name").build();

    assertThrows(
        ResourceValidationException.class, () -> certificateDao.update(expectedCertificate));
  }

  @Test
  void updatePatch() {
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    CertificateDtoWithoutTags updateCertificate = new CertificateDtoWithoutTags();
    updateCertificate.setId(id);
    updateCertificate.setName("new name");
    LocalDateTime timeNow = LocalDateTime.now();
    updateCertificate.setLastUpdateDate(timeNow);
    CertificateDtoWithTags expectedCertificate = givenExistingCertificate1();
    expectedCertificate.setId(id);
    expectedCertificate.setName(updateCertificate.getName());
    expectedCertificate.setLastUpdateDate(timeNow);

    certificateDao.updatePresentedFields(updateCertificate);

    CertificateDtoWithTags actualCertificate = certificateDao.read(id).get();

    assertEquals(expectedCertificate, actualCertificate);
  }

  @Test
  void updatePatchNotExisted() {
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    CertificateDtoWithoutTags updateCertificate = new CertificateDtoWithoutTags();
    updateCertificate.setId(certificate.getId());
    updateCertificate.setName("new name");

    assertThrows(
        ResourceValidationException.class,
        () -> certificateDao.updatePresentedFields(updateCertificate));
  }

  @Test
  void delete() {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    certificateDao.delete(id);

    Optional<CertificateDtoWithTags> actualCertificate = certificateDao.read(id);
    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  void deleteNotExisted() {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();

    assertThrows(
        ResourceValidationException.class, () -> certificateDao.delete(certificate1.getId()));
  }

  @Test
  void addTag() {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
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
    CertificateDtoWithTags certificate2 = givenExistingCertificate2();
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

  private static CertificateDtoWithTags givenExistingCertificate1() {
    return CertificateDtoWithTags.builder()
        .id(1L)
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }

  private static CertificateDtoWithTags givenExistingCertificate2() {
    return CertificateDtoWithTags.builder()
        .id(2L)
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static CertificateDtoWithTags givenNewCertificateWithoutId() {
    return CertificateDtoWithTags.builder()
        .name("third certificate")
        .description("third description")
        .price(3.33)
        .duration(15)
        .createDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static TagDto givenExistingTag1() {
    return TagDto.builder().id(1L).name("first tag").build();
  }

  private static TagDto givenExistingTag2() {
    return TagDto.builder().id(2L).name("second tag").build();
  }
}
