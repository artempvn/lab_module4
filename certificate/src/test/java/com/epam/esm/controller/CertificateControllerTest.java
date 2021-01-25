package com.epam.esm.controller;

import com.epam.esm.advice.ResourceAdvice;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.HibernateSessionFactoryUtil;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.CertificateDtoWithTags;
import com.epam.esm.entity.CertificateDtoWithoutTags;
import com.epam.esm.entity.TagDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@AutoConfigureTestDatabase
@SpringBootTest
class CertificateControllerTest {
  MockMvc mockMvc;
  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired CertificateController certificateController;
  @Autowired HibernateSessionFactoryUtil factoryUtil;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(certificateController)
            .setControllerAdvice(new ResourceAdvice())
            .build();
  }

  @AfterEach
  void setDown() {
    Session session = factoryUtil.getSessionFactory().openSession();
    session.beginTransaction();
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    session.createNativeQuery(sql).executeUpdate();
    session.getTransaction().commit();
    session.close();
  }

  @Test
  void readCertificatePositiveStatusCheck() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();

    mockMvc.perform(get("/certificates/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void readCertificateNegativeStatusCheck() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();

    mockMvc
        .perform(get("/certificates/{id}", certificate1.getId()))
        .andExpect(status().isNotFound());
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

    mockMvc.perform(get("/certificates")).andExpect(status().isOk());
  }

  @Test
  void readCertificatesValueCheck() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    CertificateDtoWithTags certificate2 = givenExistingCertificate2();
    long certificateId1 = certificateDao.create(certificate1).getId();
    long certificateId2 = certificateDao.create(certificate2).getId();
    CertificateDtoWithoutTags c1 = givenExistingCertificate1WT();
    CertificateDtoWithoutTags c2 = givenExistingCertificate2WT();
    c1.setId(certificateId1);
    c2.setId(certificateId2);

    mockMvc
        .perform(get("/certificates"))
        .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(c1, c2))));
  }

  @Test
  void readAll() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    CertificateDtoWithTags certificate2 = givenExistingCertificate2();
    long certificateId1 = certificateDao.create(certificate1).getId();
    long certificateId2 = certificateDao.create(certificate2).getId();
    CertificateDtoWithoutTags c1 = givenExistingCertificate1WT();
    CertificateDtoWithoutTags c2 = givenExistingCertificate2WT();
    c1.setId(certificateId1);
    c2.setId(certificateId2);

    mockMvc
        .perform(get("/certificates?name=cert"))
        .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(c1, c2))));
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
            put("/certificates/{id}", certificateUpdate.getId())
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
            patch("/certificates/{id}", certificateUpdate.getId())
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateCertificatePatchPositiveValueCheck() throws Exception {
    CertificateDtoWithTags certificate = givenExistingCertificate1();
    long id = certificateDao.create(certificate).getId();
    CertificateDtoWithoutTags certificateUpdate = givenExistingCertificate2WT();
    certificateUpdate.setId(id);

    mockMvc
        .perform(
            patch("/certificates/{id}", id)
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value(certificateUpdate.getName()))
        .andExpect(jsonPath("$.description").value(certificateUpdate.getDescription()))
        .andExpect(jsonPath("$.price").value(certificateUpdate.getPrice()))
        .andExpect(jsonPath("$.duration").value(certificateUpdate.getDuration()));
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
    CertificateDtoWithTags certificate = givenExistingCertificate1();

    mockMvc
        .perform(delete("/certificates/{id}", certificate.getId()))
        .andExpect(status().isBadRequest());
  }

  private static CertificateDtoWithTags givenExistingCertificate1() {
    return CertificateDtoWithTags.builder()
        .id(1L)
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 30, 10))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }

  private static CertificateDtoWithTags givenExistingCertificate2() {
    return CertificateDtoWithTags.builder()
        .id(2L)
        .name("second certificate")
        .description("second description")
        //        .price(2.33)
        //        .duration(10)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static CertificateDtoWithoutTags givenExistingCertificate1WT() {
    return CertificateDtoWithoutTags.builder()
        .id(1L)
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 30, 10))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }

  private static CertificateDtoWithoutTags givenExistingCertificate2WT() {
    return CertificateDtoWithoutTags.builder()
        .id(2L)
        .name("second certificate")
        .description("second description")
        //        .price(2.33)
        //        .duration(10)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static CertificateDtoWithTags givenNewCertificateForUpdatePutId1() {
    return CertificateDtoWithTags.builder()
        .id(1L)
        .name("new name")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }

  private static CertificateDtoWithTags givenNewCertificateForUpdateId1() {
    return CertificateDtoWithTags.builder().id(1L).name("new name").build();
  }

  private static TagDto givenExistingTag1() {
    return TagDto.builder().id(1L).name("first tag").build();
  }

  private static TagDto givenExistingTag2() {
    return TagDto.builder().id(2L).name("second tag").build();
  }
}
