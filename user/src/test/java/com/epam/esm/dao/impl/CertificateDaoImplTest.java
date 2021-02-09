package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class CertificateDaoImplTest {

  @Autowired OrderDao orderDao;

  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;
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
    Tag tag = givenTag();
    long id = tagDao.create(tag).getId();
    tag.setId(id);
    Certificate expectedCertificate = givenCertificate();
    expectedCertificate.setTags(List.of(tag));

    Certificate actualCertificate = certificateDao.create(expectedCertificate);

    assertNotNull(actualCertificate.getId());
  }

  Certificate givenCertificate() {
    Certificate certificate = new Certificate();
    certificate.setPreviousId(99L);
    certificate.setPrice(99.99);
    var tag = givenTag();
    certificate.setTags(List.of(tag));
    return certificate;
  }

  Tag givenTag() {
    Tag tag = new Tag();
    tag.setName("tag name");
    return tag;
  }
}
