package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@SpringBootTest
class CertificateDaoImplTest {

  public static final long ID_FOR_3_CERTIFICATE = 3L;
  public static final int NOT_EXISTED_CERTIFICATE_ID = 99999;

  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;

  @BeforeEach
  void setUp() {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate certificate2 = givenExistingCertificate2();
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);
    tagDao.create(tag1);
    tagDao.create(tag2);
    certificateDao.addTag(tag1.getId(), certificate2.getId());
    certificateDao.addTag(tag2.getId(), certificate2.getId());
  }

  @Test
  void create() {
    Certificate expectedCertificate = givenNewCertificateWithoutId();

    Certificate actualCertificate = certificateDao.create(expectedCertificate);

    assertEquals(expectedCertificate, actualCertificate);
  }

  @ParameterizedTest
  @MethodSource("readDataProvider")
  void read(long actualId, Optional<Certificate> expectedCertificate) {
    Optional<Certificate> actualCertificate = certificateDao.read(actualId);

    assertEquals(expectedCertificate, actualCertificate);
  }

  static Stream<Arguments> readDataProvider() {
    return Stream.of(
        arguments(givenExistingCertificate1().getId(), Optional.of(givenExistingCertificate1())),
        arguments(NOT_EXISTED_CERTIFICATE_ID, Optional.empty()));
  }

  @Test
  void readAll() {
    Certificate certificate1 = givenExistingCertificate1();
    Tag tag1=givenExistingTag1();
    Tag tag2=givenExistingTag2();
    Certificate certificate2 = givenExistingCertificate2();
    certificate2.setTags(List.of(tag1,tag2));
    List<Certificate> expectedList = List.of(certificate1, certificate2);

    List<Certificate> actualList = certificateDao.readAll();
    assertEquals(expectedList, actualList);
  }

  @Test
  void update() {
    Certificate expectedCertificate = Certificate.builder().id(1L).name("new name").build();

    certificateDao.update(expectedCertificate);

    Certificate actualCertificate = certificateDao.read(expectedCertificate.getId()).get();
    assertEquals(expectedCertificate, actualCertificate);
  }

  @Test
  void delete() {
    Certificate certificate = givenExistingCertificate1();

    certificateDao.delete(certificate.getId());

    Optional<Certificate> actualCertificate = certificateDao.read(certificate.getId());
    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  void readTags() {
    Certificate certificate = givenExistingCertificate2();
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    List<Tag> expectedTags = List.of(tag1, tag2);

    List<Tag> actualTags = certificateDao.readCertificateTags(certificate.getId());
    assertEquals(expectedTags, actualTags);
  }

  @Test
  void addTag() {
    Certificate certificate = givenExistingCertificate1();
    Tag tag = givenExistingTag1();
    List<Tag> expectedTags = List.of(tag);

    certificateDao.addTag(tag.getId(), certificate.getId());

    List<Tag> actualTags = certificateDao.readCertificateTags(certificate.getId());
    assertEquals(expectedTags, actualTags);
  }

  @Test
  void removeTag() {
    Certificate certificate = givenExistingCertificate2();
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    List<Tag> expectedTags = List.of(tag1);

    certificateDao.removeTag(tag2.getId(), certificate.getId());

    List<Tag> actualTags = certificateDao.readCertificateTags(certificate.getId());
    assertEquals(expectedTags, actualTags);
  }

  @Test
  void deleteCertificateTagsByTagId() {
    Certificate certificate = givenExistingCertificate2();
    Tag tag = givenExistingTag1();
    List<Tag> expectedTags = List.of(tag);

    certificateDao.deleteCertificateTagsByTagId(certificate.getId());

    List<Tag> actualTags = certificateDao.readCertificateTags(certificate.getId());
    assertEquals(expectedTags, actualTags);
  }

  @Test
  void deleteCertificateTagsByCertificateId() {
    Certificate certificate = givenExistingCertificate2();
    List<Tag> expectedTags = Collections.emptyList();

    certificateDao.deleteCertificateTagsByCertificateId(certificate.getId());

    List<Tag> actualTags = certificateDao.readCertificateTags(certificate.getId());
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
