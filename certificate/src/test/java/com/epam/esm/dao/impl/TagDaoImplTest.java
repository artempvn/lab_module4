package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ResourceValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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
class TagDaoImplTest {

  public static final int NOT_EXISTED_TAG_ID = 9999999;
  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;

  @AfterEach
  void setDown() {
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
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
    tagDao.create(tag1);
    List<TagDto> expectedList = List.of(tag1);
    PaginationParameter parameter = new PaginationParameter();
    parameter.setPage(1);
    parameter.setSize(10);

    PageData<TagDto> actualPage = tagDao.readAll(parameter);

    assertEquals(expectedList.size(), actualPage.getContent().size());
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

    assertThrows(ResourceValidationException.class, () -> tagDao.delete(NOT_EXISTED_TAG_ID));
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
    return TagDto.builder().name("first tag").build();
  }

  private static TagDto givenNewTagWithoutId() {
    return TagDto.builder().name("third tag").build();
  }

  private static CertificateDtoWithTags givenExistingCertificate1() {
    return CertificateDtoWithTags.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }
}
