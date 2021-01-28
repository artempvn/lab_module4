package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateDto;
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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class CertificateDaoImplTest {

    @Autowired
    OrderDao orderDao;

    @Autowired
    CertificateDao certificateDao;
    @Autowired
    TagDao tagDao;
    @Autowired
    SessionFactory sessionFactory;

    @AfterEach
    void setDown() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String sql = "DELETE FROM certificates_tags_backup;DELETE FROM tags_backup;DELETE FROM orders;" +
                    "DELETE FROM certificates_backup;DELETE FROM users;";
            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void create() {
        TagDto tag=givenTag();
        long id=tagDao.create(tag).getId();
        tag.setId(id);
        CertificateDto expectedCertificate = givenCertificate();
        expectedCertificate.setTags(List.of(tag));

        CertificateDto actualCertificate = certificateDao.create(expectedCertificate);

        assertNotNull(actualCertificate.getId());
    }

    CertificateDto givenCertificate(){
        CertificateDto certificate=new CertificateDto();
        certificate.setPreviousId(99L);
        certificate.setPrice(99.99);
        var tag=givenTag();
        certificate.setTags(List.of(tag));
        return certificate;
    }

    TagDto givenTag(){
        TagDto tag=new TagDto();
        tag.setName("tag name");
        return tag;
    }

}