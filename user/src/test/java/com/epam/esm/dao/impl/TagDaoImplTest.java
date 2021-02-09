package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class TagDaoImplTest {

  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;

  @AfterEach
  void setDown() {
    String sql =
        "DELETE FROM ordered_certificates_tags;DELETE FROM ordered_tags;DELETE FROM ordered_certificates;"
            + "DELETE FROM orders;DELETE FROM users;";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  void create() {
    Tag expectedTag = givenTag();

    Tag actualTag = tagDao.create(expectedTag);

    assertNotNull(actualTag.getId());
  }

  @Test
  void readExistedByName() {
    Tag expectedTag = givenTag();
    tagDao.create(expectedTag);

    Optional<Tag> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isPresent());
  }

  @Test
  void readNotExistedByName() {
    Tag expectedTag = givenTag();
    Optional<Tag> actualTag = tagDao.read(expectedTag.getName());

    assertTrue(actualTag.isEmpty());
  }

  Tag givenTag() {
    Tag tag = new Tag();
    tag.setName("tag name");
    return tag;
  }
}
