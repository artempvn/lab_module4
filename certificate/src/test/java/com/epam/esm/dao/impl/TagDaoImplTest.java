package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.CertificateDtoWithTags;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

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
  @Autowired CertificateDao certificateDao;
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
    TagDto expectedTag = givenNewTagWithoutId();

    Tag actualTag = tagDao.create(expectedTag);

    assertNotNull(actualTag.getId());
  }

  @Test
  void readExistedById() {
    TagDto expectedTag = givenExistingTag1();
    long id = tagDao.create(expectedTag).getId();

    Optional<Tag> actualTag = tagDao.read(id);

    assertTrue(actualTag.isPresent());
  }

  @Test
  void readNotExistedById() {
    Optional<Tag> actualTag = tagDao.read(NOT_EXISTED_TAG_ID);

    assertTrue(actualTag.isEmpty());
  }

  @Test
  void readAll() {
    TagDto tag1 = givenExistingTag1();
    TagDto tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);
    List<Tag> expectedList = List.of(new Tag(tag1), new Tag(tag2));

    List<Tag> actualList = tagDao.readAll();

    assertEquals(expectedList.size(), actualList.size());
  }

  @Test
  void delete() {
    TagDto tag = givenExistingTag1();
    long id = tagDao.create(tag).getId();

    tagDao.delete(id);

    assertTrue(tagDao.read(tag.getId()).isEmpty());
  }

  @Test
  void deleteNotExisted() {
    TagDto tag = givenExistingTag1();

    assertThrows(ResourceValidationException.class, () -> tagDao.delete(tag.getId()));
  }

  @Test
  void deleteWithBoundFk() {
    TagDto tag = givenExistingTag1();
    long tagId = tagDao.create(tag).getId();
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    long certificateId = certificateDao.create(certificate).getId();
    certificateDao.addTag(tagId, certificateId);

    assertThrows(DataIntegrityViolationException.class, () -> tagDao.delete(tagId));
  }

  @Test
  void readExistedByName() {
    TagDto expectedTag = givenExistingTag1();
    tagDao.create(expectedTag);

    Optional<Tag> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isPresent());
  }

  @Test
  void readNotExistedByName() {
    TagDto expectedTag = givenExistingTag1();

    Optional<Tag> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isEmpty());
  }

  private static TagDto givenExistingTag1() {
    return TagDto.builder().id(1L).name("first tag").build();
  }

  private static TagDto givenExistingTag2() {
    return TagDto.builder().id(2L).name("second tag").build();
  }

  private static TagDto givenNewTagWithoutId() {
    return TagDto.builder().name("third tag").build();
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
}
