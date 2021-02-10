package com.epam.esm.web.rest;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class CertificateResourceSecurityTest {

  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired CertificateResource certificateController;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;
  @Autowired ReloadableResourceBundleMessageSource messageSource;
  @Autowired LocaleResolver localeResolver;
  @Autowired private WebApplicationContext context;

  @Autowired private MockMvc mockMvc;

  @AfterEach
  void setDown() {
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  void readCertificateUnauthorizedStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    mockMvc.perform(get("/certificates/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void readAllUnauthorizedStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    mockMvc.perform(get("/certificates?page=1&size=5")).andExpect(status().isOk());
  }

  @Test
  void deleteUnauthorizedStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    mockMvc.perform(delete("/certificates/{id}",id)).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteStatusCheckAuth() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    mockMvc.perform(delete("/certificates/{id}",id)).andExpect(status().isNoContent());
  }

  private static Certificate givenExistingCertificate1() {
    return Certificate.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }
}
