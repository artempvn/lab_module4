package com.epam.esm.web.rest;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificatePatchDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.web.advice.ResourceAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.LocaleResolver;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("certificate")
@AutoConfigureTestDatabase
@SpringBootTest
class CertificateResourceTest {
  public static final long NOT_EXISTING_ID = 99999L;
  MockMvc mockMvc;
  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired CertificateResource certificateController;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;

  @Autowired ReloadableResourceBundleMessageSource messageSource;
  @Autowired LocaleResolver localeResolver;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(certificateController)
            .setControllerAdvice(new ResourceAdvice(messageSource))
            .build();
  }

  @AfterEach
  void setDown() {
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  void readCertificatePositiveStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    mockMvc.perform(get("/certificates/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void readCertificateNegativeStatusCheck() throws Exception {

    mockMvc.perform(get("/certificates/{id}", NOT_EXISTING_ID)).andExpect(status().isNotFound());
  }

  @Test
  void readCertificatePositiveValueCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    long tagId1 = tagDao.create(tag1).getId();
    long tagId2 = tagDao.create(tag2).getId();
    long certificateId = certificateDao.create(certificate1).getId();
    certificate1.setId(certificateId);
    certificateDao.addTag(tagId1, certificateId);
    certificateDao.addTag(tagId2, certificateId);
    tag1.setId(tagId1);
    tag2.setId(tagId2);
    certificate1.setTags(List.of(tag1, tag2));

    mockMvc
        .perform(get("/certificates/{id}", certificateId))
        .andExpect(jsonPath("$.id").value(certificateId))
        .andExpect(jsonPath("$.name").value(certificate1.getName()))
        .andExpect(jsonPath("$.description").value(certificate1.getDescription()))
        .andExpect(jsonPath("$.price").value(certificate1.getPrice()))
        .andExpect(jsonPath("$.duration").value(certificate1.getDuration()));
  }

  @Test
  void readCertificatesStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate certificate2 = givenExistingCertificate1();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);

    mockMvc.perform(get("/certificates?page=1&size=10")).andExpect(status().isOk());
  }

  @Test
  void readCertificatesValueCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate certificate2 = givenExistingCertificate2();
    var cert1 = certificateDao.create(certificate1);
    var cert2 = certificateDao.create(certificate2);
    Certificate c1 = givenExistingCertificate1WT();
    Certificate c2 = givenExistingCertificate2WT();
    c1.setId(cert1.getId());
    c1.setCreateDate(cert1.getCreateDate());
    c1.setLastUpdateDate(cert1.getLastUpdateDate());
    c2.setId(cert2.getId());
    c2.setCreateDate(cert2.getCreateDate());
    c2.setLastUpdateDate(cert2.getLastUpdateDate());

    mockMvc
        .perform(get("/certificates?page=1&size=10"))
        .andExpect(jsonPath("$.currentPage").value(1))
        .andExpect(jsonPath("$.content").isNotEmpty())
        .andExpect(
            jsonPath(
                "$.links[?(@.rel=='self')].href",
                contains("http://localhost/certificates?page=1&size=10")));
  }

  @Test
  void readAll() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate certificate2 = givenExistingCertificate2();
    var cert1 = certificateDao.create(certificate1);
    var cert2 = certificateDao.create(certificate2);
    Certificate c1 = givenExistingCertificate1WT();
    Certificate c2 = givenExistingCertificate2WT();
    c1.setId(cert1.getId());
    c1.setCreateDate(cert1.getCreateDate());
    c1.setLastUpdateDate(cert1.getLastUpdateDate());
    c2.setId(cert2.getId());
    c2.setCreateDate(cert2.getCreateDate());
    c2.setLastUpdateDate(cert2.getLastUpdateDate());

    mockMvc
        .perform(get("/certificates?page=1&size=10&name=cert&sort.name=ASC&tags=tag1"))
        .andExpect(jsonPath("$.currentPage").value(1))
        .andExpect(jsonPath("$.content").isEmpty())
        .andExpect(
            jsonPath(
                "$.links[?(@.rel=='self')].href",
                contains(
                    "http://localhost/certificates?page=1&size=10&tags=tag1&name=cert&sort.name=ASC")));
  }

  @Test
  void createCertificateStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    certificate1.setId(null);

    mockMvc
        .perform(
            post("/certificates")
                .content(new ObjectMapper().writeValueAsString(certificate1))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  void createCertificateValueCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    certificate1.setId(null);
    Certificate certificateWithId = givenExistingCertificate1();

    mockMvc
        .perform(
            post("/certificates")
                .content(new ObjectMapper().writeValueAsString(certificate1))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value(certificateWithId.getName()))
        .andExpect(jsonPath("$.description").value(certificateWithId.getDescription()))
        .andExpect(jsonPath("$.price").value(certificateWithId.getPrice()))
        .andExpect(jsonPath("$.duration").value(certificateWithId.getDuration()));
  }

  @Test
  void updateCertificatePutPositiveStatusCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    Certificate certificateUpdate = givenNewCertificateForUpdatePutId1();

    mockMvc
        .perform(
            put("/certificates/{id}", id)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void updateCertificatePutNegativeStatusCheck() throws Exception {
    Certificate certificateUpdate = givenNewCertificateForUpdateId1();

    mockMvc
        .perform(
            put("/certificates/{id}", NOT_EXISTING_ID)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateCertificatePutPositiveValueCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    Certificate certificateUpdate = givenNewCertificateForUpdatePutId1();
    certificateUpdate.setId(id);

    mockMvc
        .perform(
            put("/certificates/{id}", id)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value(certificateUpdate.getName()))
        .andExpect(jsonPath("$.description").value(certificateUpdate.getDescription()))
        .andExpect(jsonPath("$.price").value(certificateUpdate.getPrice()))
        .andExpect(jsonPath("$.duration").value(certificateUpdate.getDuration()));
  }

  @Test
  void updateCertificatePatchPositiveStatusCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    Certificate certificateUpdate = givenExistingCertificate2WT();
    certificateUpdate.setId(id);

    mockMvc
        .perform(
            patch("/certificates/{id}", id)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void updateCertificatePatchNegativeStatusCheck() throws Exception {
    Certificate certificateUpdate = givenExistingCertificate1WT();

    mockMvc
        .perform(
            patch("/certificates/{id}", NOT_EXISTING_ID)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateCertificatePatchPositiveValueCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    CertificatePatchDto certificateUpdate = certificateForPatch();
    certificateUpdate.setId(id);

    mockMvc
        .perform(
            patch("/certificates/{id}", id)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value(certificateUpdate.getName()))
        .andExpect(jsonPath("$.price").value(certificateUpdate.getPrice()));
  }

  @Test
  void deleteCertificateStatusCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();

    mockMvc.perform(delete("/certificates/{id}", id)).andExpect(status().isNoContent());
  }

  @Test
  void deleteCertificateValueCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();

    mockMvc.perform(delete("/certificates/{id}", id));
    mockMvc.perform(get("/certificates/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void deleteCertificateNegative() throws Exception {

    mockMvc
        .perform(delete("/certificates/{id}", NOT_EXISTING_ID))
        .andExpect(status().isBadRequest());
  }

  private static Certificate givenExistingCertificate1() {
    return Certificate.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }

  private static Certificate givenExistingCertificate2() {
    return Certificate.builder()
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .build();
  }

  private static Certificate givenExistingCertificate1WT() {
    return Certificate.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }

  private static Certificate givenExistingCertificate2WT() {
    return Certificate.builder()
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .build();
  }

  private static CertificatePatchDto certificateForPatch() {
    CertificatePatchDto certificateDtoPatch = new CertificatePatchDto();
    certificateDtoPatch.setName("new name");
    certificateDtoPatch.setPrice(50.5);
    return certificateDtoPatch;
  }

  private static Certificate givenNewCertificateForUpdatePutId1() {
    return Certificate.builder()
        .name("new name")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }

  private static Certificate givenNewCertificateForUpdateId1() {
    return Certificate.builder().name("new name").build();
  }

  private static Tag givenExistingTag1() {
    return Tag.builder().name("first tag").build();
  }

  private static Tag givenExistingTag2() {
    return Tag.builder().name("second tag").build();
  }
}
