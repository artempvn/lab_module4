package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.TagDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class CertificateDaoImplTest {

  @Autowired OrderDao orderDao;

  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;
  @Autowired SessionFactory sessionFactory;

  @AfterEach
  void setDown() {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      String sql =
              "DELETE FROM ordered_certificates_tags;DELETE FROM ordered_tags;DELETE FROM ordered_certificates;"
                      + "DELETE FROM orders;DELETE FROM users;";
      session.createNativeQuery(sql).executeUpdate();
      session.getTransaction().commit();
    }
  }

  @Test
  void create() {
    TagDto tag = givenTag();
    long id = tagDao.create(tag).getId();
    tag.setId(id);
    CertificateDtoWithTags expectedCertificate = givenCertificate();
    expectedCertificate.setTags(List.of(tag));

    CertificateDtoWithTags actualCertificate = certificateDao.create(expectedCertificate);

    assertNotNull(actualCertificate.getId());
  }

  CertificateDtoWithTags givenCertificate() {
    CertificateDtoWithTags certificate = new CertificateDtoWithTags();
    certificate.setPreviousId(99L);
    certificate.setPrice(99.99);
    var tag = givenTag();
    certificate.setTags(List.of(tag));
    return certificate;
  }

  TagDto givenTag() {
    TagDto tag = new TagDto();
    tag.setName("tag name");
    return tag;
  }
}
