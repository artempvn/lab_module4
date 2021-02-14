package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    Certificate expectedCertificate = givenNewCertificateWithoutId();

    Certificate actualCertificate = certificateDao.create(expectedCertificate);

    assertNotNull(actualCertificate.getId());
  }

  @Test
  void readExisted() {
    Certificate certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    Optional<Certificate> actualCertificate = certificateDao.read(id);

    assertTrue(actualCertificate.isPresent());
  }

  @Test
  void readNotExisted() {
    Optional<Certificate> actualCertificate = certificateDao.read(NOT_EXISTED_CERTIFICATE_ID);

    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  void readAll() {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate certificate2 = givenExistingCertificate2();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);
    List<Certificate> expectedList = List.of(certificate1, certificate2);
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    parameter.setSize(10);

    PageData<Certificate> actualPage = certificateDao.readAll(new CertificatesRequest(), parameter);
    assertEquals(expectedList.size(), actualPage.getContent().size());
  }

  @Test
  void update() {
    Certificate certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();
    Certificate expectedCertificate = Certificate.builder().id(id).name("new name").build();

    certificateDao.update(expectedCertificate);

    Certificate actualCertificate = certificateDao.read(id).get();
    assertEquals(expectedCertificate.getName(), actualCertificate.getName());
  }

  @Test
  @Transactional
  void updatePatch() {
    Certificate certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    Certificate updateCertificate = new Certificate();
    updateCertificate.setId(id);
    updateCertificate.setName("new name");
    Certificate expectedCertificate = givenExistingCertificate1();
    expectedCertificate.setId(id);
    expectedCertificate.setName(updateCertificate.getName());
    entityManager.clear();

    certificateDao.updatePresentedFields(updateCertificate);

    Certificate actualCertificate = certificateDao.read(id).get();

    assertEquals(expectedCertificate, actualCertificate);
  }

  @Test
  void delete() {
    Certificate certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    certificateDao.delete(id);

    Optional<Certificate> actualCertificate = certificateDao.read(id);
    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  @Transactional
  void addTag() {
    Certificate certificate1 = givenExistingCertificate1();
    Tag tag1 = givenExistingTag1();
    long certificateId = certificateDao.create(certificate1).getId();
    long tagId = tagDao.create(tag1).getId();
    List<Tag> expectedTags = List.of(tag1);
    entityManager.clear();

    certificateDao.addTag(tagId, certificateId);

    List<Tag> actualTags = certificateDao.read(certificateId).get().getTags();
    assertEquals(expectedTags.size(), actualTags.size());
  }

  @Test
  @Transactional
  void removeTag() {
    Certificate certificate2 = givenExistingCertificate2();
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    long certificateId = certificateDao.create(certificate2).getId();
    long tagId1 = tagDao.create(tag1).getId();
    long tagId2 = tagDao.create(tag2).getId();
    certificateDao.addTag(tagId1, certificateId);
    certificateDao.addTag(tagId2, certificateId);
    tag1.setId(tagId1);
    List<Tag> expectedTags = List.of(tag1);
    entityManager.clear();

    certificateDao.removeTag(tagId2, certificateId);

    List<Tag> actualTags = certificateDao.read(certificateId).get().getTags();
    assertEquals(expectedTags, actualTags);
  }

  private static Certificate givenExistingCertificate1() {
    return Certificate.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }

  private static Certificate givenExistingCertificate2() {
    return Certificate.builder()
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static Certificate givenNewCertificateWithoutId() {
    return Certificate.builder()
        .name("third certificate")
        .description("third description")
        .price(3.33)
        .duration(15)
        .createDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static Tag givenExistingTag1() {
    return Tag.builder().name("first tag").build();
  }

  private static Tag givenExistingTag2() {
    return Tag.builder().name("second tag").build();
  }
}
