package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ResourceValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("certificate")
@AutoConfigureTestDatabase
@SpringBootTest
class TagDaoImplTest {

  public static final int NOT_EXISTED_TAG_ID = 9999999;
  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
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
    TagDto expectedTag = givenNewTagWithoutId();

    TagDto actualTag = tagDao.create(expectedTag);

    assertNotNull(actualTag.getId());
  }

  @Test
  void readExistedById() {
    TagDto expectedTag = givenExistingTag1();
    long id = tagDao.create(expectedTag).getId();

    Optional<TagDto> actualTag = tagDao.read(id);

    assertTrue(actualTag.isPresent());
  }

  @Test
  void readNotExistedById() {
    Optional<TagDto> actualTag = tagDao.read(NOT_EXISTED_TAG_ID);

    assertTrue(actualTag.isEmpty());
  }

  @Test
  void readAll() {
    TagDto tag1 = givenExistingTag1();
    TagDto tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);
    List<TagDto> expectedList = List.of(tag1, tag2);

    List<TagDto> actualList = tagDao.readAll(new PaginationParameter());

    assertEquals(expectedList.size(), actualList.size());
  }

  @Test
  void delete() {
    TagDto tag = givenExistingTag1();
    long id = tagDao.create(tag).getId();

    tagDao.delete(id);

    assertTrue(tagDao.read(id).isEmpty());
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

    assertThrows(JpaSystemException.class, () -> tagDao.delete(tagId));
  }

  @Test
  void readExistedByName() {
    TagDto expectedTag = givenExistingTag1();
    tagDao.create(expectedTag);

    Optional<TagDto> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isPresent());
  }

  @Test
  void readNotExistedByName() {
    TagDto expectedTag = givenExistingTag1();

    Optional<TagDto> actualTag = tagDao.read(expectedTag.getName());

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
