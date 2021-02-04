package com.epam.esm.web.rest;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDtoPatch;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.CertificateDtoWithoutTags;
import com.epam.esm.dto.TagDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    mockMvc.perform(get("/certificates/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void readCertificateNegativeStatusCheck() throws Exception {

    mockMvc.perform(get("/certificates/{id}", NOT_EXISTING_ID)).andExpect(status().isNotFound());
  }

  @Test
  void readCertificatePositiveValueCheck() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    TagDto tag1 = givenExistingTag1();
    TagDto tag2 = givenExistingTag2();
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
        .andExpect(content().json(new ObjectMapper().writeValueAsString(certificate1)));
  }

  @Test
  void readCertificatesStatusCheck() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    CertificateDtoWithTags certificate2 = givenExistingCertificate1();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);

    mockMvc.perform(get("/certificates?page=1&size=10")).andExpect(status().isOk());
  }

  @Test
  void readCertificatesValueCheck() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    CertificateDtoWithTags certificate2 = givenExistingCertificate2();
    var cert1 = certificateDao.create(certificate1);
    var cert2 = certificateDao.create(certificate2);
    CertificateDtoWithoutTags c1 = givenExistingCertificate1WT();
    CertificateDtoWithoutTags c2 = givenExistingCertificate2WT();
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
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    CertificateDtoWithTags certificate2 = givenExistingCertificate2();
    var cert1 = certificateDao.create(certificate1);
    var cert2 = certificateDao.create(certificate2);
    CertificateDtoWithoutTags c1 = givenExistingCertificate1WT();
    CertificateDtoWithoutTags c2 = givenExistingCertificate2WT();
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
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
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
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    certificate1.setId(null);
    CertificateDtoWithTags certificateWithId = givenExistingCertificate1();

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
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    CertificateDtoWithTags certificateUpdate = givenNewCertificateForUpdatePutId1();

    mockMvc
        .perform(
            put("/certificates/{id}", id)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void updateCertificatePutNegativeStatusCheck() throws Exception {
    CertificateDtoWithTags certificateUpdate = givenNewCertificateForUpdateId1();

    mockMvc
        .perform(
            put("/certificates/{id}", NOT_EXISTING_ID)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateCertificatePutPositiveValueCheck() throws Exception {
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    CertificateDtoWithTags certificateUpdate = givenNewCertificateForUpdatePutId1();
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
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    CertificateDtoWithoutTags certificateUpdate = givenExistingCertificate2WT();
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
    CertificateDtoWithoutTags certificateUpdate = givenExistingCertificate1WT();

    mockMvc
        .perform(
            patch("/certificates/{id}", NOT_EXISTING_ID)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateCertificatePatchPositiveValueCheck() throws Exception {
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    CertificateDtoPatch certificateUpdate = certificateForPatch();
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
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();

    mockMvc.perform(delete("/certificates/{id}", id)).andExpect(status().isNoContent());
  }

  @Test
  void deleteCertificateValueCheck() throws Exception {
    CertificateDtoWithTags certificate = givenExistingCertificate1();
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

  private static CertificateDtoWithTags givenExistingCertificate1() {
    return CertificateDtoWithTags.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }

  private static CertificateDtoWithTags givenExistingCertificate2() {
    return CertificateDtoWithTags.builder()
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .build();
  }

  private static CertificateDtoWithoutTags givenExistingCertificate1WT() {
    return CertificateDtoWithoutTags.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }

  private static CertificateDtoWithoutTags givenExistingCertificate2WT() {
    return CertificateDtoWithoutTags.builder()
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .build();
  }

  private static CertificateDtoPatch certificateForPatch() {
    CertificateDtoPatch certificateDtoPatch = new CertificateDtoPatch();
    certificateDtoPatch.setName("new name");
    certificateDtoPatch.setPrice(50.5);
    return certificateDtoPatch;
  }

  private static CertificateDtoWithTags givenNewCertificateForUpdatePutId1() {
    return CertificateDtoWithTags.builder()
        .name("new name")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }

  private static CertificateDtoWithTags givenNewCertificateForUpdateId1() {
    return CertificateDtoWithTags.builder().name("new name").build();
  }

  private static TagDto givenExistingTag1() {
    return TagDto.builder().name("first tag").build();
  }

  private static TagDto givenExistingTag2() {
    return TagDto.builder().name("second tag").build();
  }
}
