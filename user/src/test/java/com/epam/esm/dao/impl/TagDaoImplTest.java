package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
class TagDaoImplTest {

  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired EntityManager entityManager;

  @AfterEach
  void setDown() {
    Session session = entityManager.unwrap(Session.class);
    String sql =
        "DELETE FROM ordered_certificates_tags;DELETE FROM ordered_tags;DELETE FROM ordered_certificates;"
            + "DELETE FROM orders;DELETE FROM users;";
    session.createNativeQuery(sql).executeUpdate();
  }

  @Test
  void create() {
    TagDto expectedTag = givenTag();

    TagDto actualTag = tagDao.create(expectedTag);

    assertNotNull(actualTag.getId());
  }

  @Test
  void readExistedByName() {
    TagDto expectedTag = givenTag();
    tagDao.create(expectedTag);

    Optional<TagDto> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isPresent());
  }

  @Test
  void readNotExistedByName() {
    TagDto expectedTag = givenTag();
    Optional<TagDto> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isEmpty());
  }

  TagDto givenTag() {
    TagDto tag = new TagDto();
    tag.setName("tag name");
    return tag;
  }
}
