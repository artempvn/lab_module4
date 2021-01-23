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
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@AutoConfigureTestDatabase
@SpringBootTest
class TagDaoImplTest {

  public static final int NOT_EXISTED_TAG_ID = 9999999;
  @Autowired TagDao tagDao;
  @Autowired
  CertificateDao certificateDao;
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
    Tag expectedTag = givenNewTagWithoutId();

    Tag actualTag = tagDao.create(expectedTag);

    assertNotNull(actualTag.getId());
  }

  @Test
  void readExistedById() {
    Tag expectedTag = givenExistingTag1();
    tagDao.create(expectedTag);

    Optional<Tag> actualTag = tagDao.read(expectedTag.getId());

    assertTrue(actualTag.isPresent());
  }

  @Test
  void readNotExistedById() {
    Optional<Tag> actualTag = tagDao.read(NOT_EXISTED_TAG_ID);

    assertTrue(actualTag.isEmpty());
  }

  @Test
  void readAll() {
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);
    List<Tag> expectedList = List.of(tag1, tag2);

    List<Tag> actualList = tagDao.readAll();

    assertEquals(expectedList, actualList);
  }


  @Test
  void delete() {
    Tag tag = givenExistingTag1();
    tagDao.create(tag);

    tagDao.delete(tag.getId());

    assertTrue(tagDao.read(tag.getId()).isEmpty());
  }


  @Test
  void deleteNotExisted() {
    Tag tag = givenExistingTag1();

    assertThrows(ResourceValidationException.class, () -> tagDao.delete(tag.getId()));
  }

  @Test
  void deleteWithBoundFk() {
    Tag tag = givenExistingTag1();
    tagDao.create(tag);
    Certificate certificate=givenExistingCertificate1();
    certificateDao.create(certificate);
    certificateDao.addTag(tag.getId(),certificate.getId());

    assertThrows(DataIntegrityViolationException.class, () -> tagDao.delete(tag.getId()));
  }

  @Test
  void readExistedByName() {
    Tag expectedTag = givenExistingTag1();
    tagDao.create(expectedTag);

    Optional<Tag> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isPresent());
  }

  @Test
  void readNotExistedByName() {
    Tag expectedTag = givenExistingTag1();

    Optional<Tag> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isEmpty());
  }

  private static Tag givenExistingTag1() {
    return Tag.builder().id(1L).name("first tag").build();
  }

  private static Tag givenExistingTag2() {
    return Tag.builder().id(2L).name("second tag").build();
  }

  private static Tag givenNewTagWithoutId() {
    return Tag.builder().name("third tag").build();
  }

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
}
