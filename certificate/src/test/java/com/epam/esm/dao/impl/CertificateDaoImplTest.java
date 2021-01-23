package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@AutoConfigureTestDatabase
@SpringBootTest
class CertificateDaoImplTest {

  public static final int NOT_EXISTED_CERTIFICATE_ID = 9999999;

  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;
  @Autowired HibernateSessionFactoryUtil factoryUtil;

  @AfterEach
  void setDown() {
    Session session = factoryUtil.getSessionFactory().openSession();
    session.beginTransaction();
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    session.createNativeQuery(sql).executeUpdate();
    session.getTransaction().commit();
    session.close();
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
    certificateDao.create(certificate1);

    Optional<Certificate> actualCertificate = certificateDao.read(certificate1.getId());

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

    List<Certificate> actualList = certificateDao.readAll();
    assertEquals(expectedList.size(), actualList.size());
  }

  @Test
  void update() {
    Certificate certificate1 = givenExistingCertificate1();
    certificateDao.create(certificate1);
    Certificate expectedCertificate =
        Certificate.builder().id(certificate1.getId()).name("new name").build();

    certificateDao.update(expectedCertificate);

    Certificate actualCertificate = certificateDao.read(expectedCertificate.getId()).get();
    assertEquals(expectedCertificate.getName(), actualCertificate.getName());
  }

  @Test
  void updateNotExisted() {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate expectedCertificate =
        Certificate.builder().id(certificate1.getId()).name("new name").build();

    assertThrows(
        ObjectOptimisticLockingFailureException.class,
        () -> certificateDao.update(expectedCertificate));
  }

  @Test
  void delete() {
    Certificate certificate1 = givenExistingCertificate1();
    certificateDao.create(certificate1);

    certificateDao.delete(certificate1.getId());

    Optional<Certificate> actualCertificate = certificateDao.read(certificate1.getId());
    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  void deleteNotExisted() {
    Certificate certificate1 = givenExistingCertificate1();

    assertThrows(
        ResourceValidationException.class, () -> certificateDao.delete(certificate1.getId()));
  }

    @Test
    void addTag() {
      Certificate certificate1 = givenExistingCertificate1();
      Tag tag1 = givenExistingTag1();
      certificateDao.create(certificate1);
      tagDao.create(tag1);
      List<Tag> expectedTags = List.of(tag1);

      certificateDao.addTag(tag1.getId(), certificate1.getId());

      List<Tag> actualTags = certificateDao.read(certificate1.getId()).get().getTags();
      assertEquals(expectedTags, actualTags);
    }

    @Test
    void removeTag() {
      Certificate certificate2 = givenExistingCertificate2();
      Tag tag1 = givenExistingTag1();
      Tag tag2 = givenExistingTag2();
      certificateDao.create(certificate2);
      tagDao.create(tag1);
      tagDao.create(tag2);
      certificateDao.addTag(tag1.getId(), certificate2.getId());
      certificateDao.addTag(tag2.getId(), certificate2.getId());
      List<Tag> expectedTags = List.of(tag1);

      certificateDao.removeTag(tag2.getId(), certificate2.getId());

      List<Tag> actualTags = certificateDao.read(certificate2.getId()).get().getTags();
      assertEquals(expectedTags, actualTags);
    }

  //  @Test
  //  void updatePatch() {
  //    Certificate expectedCertificate = givenExistingCertificate1();
  //    expectedCertificate.setName("new name");
  //    CertificatePatch updateCertificate = new CertificatePatch();
  //    updateCertificate.setId(1L);
  //    updateCertificate.setName("new name");
  //
  //    certificateDao.updatePatch(updateCertificate);
  //
  //    Certificate actualCertificate = certificateDao.read(expectedCertificate.getId()).get();
  //    assertEquals(expectedCertificate, actualCertificate);
  //  }

  private static Certificate givenExistingCertificate1() {
    return Certificate.builder()
        .id(1L)
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
        .id(2L)
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
    return Tag.builder().id(1L).name("first tag").build();
  }

  private static Tag givenExistingTag2() {
    return Tag.builder().id(2L).name("second tag").build();
  }
}
