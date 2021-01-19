package com.epam.esm.controller;

import com.epam.esm.advice.ResourceAdvice;
import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
class CertificateControllerTest {
  MockMvc mockMvc;
  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired CertificateController certificateController;
  @Autowired Validator validator;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(certificateController)
            .setValidator(validator)
            .setControllerAdvice(new ResourceAdvice())
            .build();
  }

  @Test
  void readCertificatePositiveStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    certificateDao.create(certificate1);

    mockMvc.perform(get("/certificates/{id}", certificate1.getId())).andExpect(status().isOk());
  }

  @Test
  void readCertificateNegativeStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();

    mockMvc
        .perform(get("/certificates/{id}", certificate1.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  void readCertificatePositiveValueCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);
    certificate1.setId(null);
    certificateDao.create(certificate1);
    certificateDao.addTag(tag1.getId(), certificate1.getId());
    certificateDao.addTag(tag2.getId(), certificate1.getId());
    certificate1.setTags(List.of(tag1, tag2));

    mockMvc
        .perform(get("/certificates/{id}", certificate1.getId()))
        .andExpect(content().json(new ObjectMapper().writeValueAsString(certificate1)));
  }

  @Test
  void readCertificatesStatusCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate certificate2 = givenExistingCertificate1();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);

    mockMvc.perform(get("/certificates")).andExpect(status().isOk());
  }

  @Test
  void readCertificatesValueCheck() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate certificate2 = givenExistingCertificate1();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);
    certificateDao.addTag(tag1.getId(), certificate1.getId());
    certificateDao.addTag(tag2.getId(), certificate1.getId());
    certificate1.setTags(List.of(tag1, tag2));
    certificate2.setTags(Collections.emptyList());

    mockMvc
        .perform(get("/certificates"))
        .andExpect(
            content()
                .json(new ObjectMapper().writeValueAsString(List.of(certificate1, certificate2))));
  }

  @Test
  void readAll() throws Exception {
    Certificate certificate1 = givenExistingCertificate1();
    Certificate certificate2 = givenExistingCertificate2();
    certificateDao.create(certificate1);
    certificateDao.create(certificate2);
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);
    certificateDao.addTag(tag1.getId(), certificate1.getId());
    certificateDao.addTag(tag2.getId(), certificate1.getId());
    certificateDao.addTag(tag1.getId(), certificate2.getId());
    certificateDao.addTag(tag2.getId(), certificate2.getId());
    certificate1.setTags(List.of(tag1, tag2));
    certificate2.setTags(List.of(tag1, tag2));

    mockMvc
        .perform(
            get(
                "/certificates?tagName=first tag&certificateName=cert&certificateDescription=descr&sort.byName=ASC&sort.byDate=DESC"))
        .andExpect(
            content()
                .json(new ObjectMapper().writeValueAsString(List.of(certificate1, certificate2))));
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
        .andExpect(jsonPath("$.id").value(certificateWithId.getId()))
        .andExpect(jsonPath("$.name").value(certificateWithId.getName()))
        .andExpect(jsonPath("$.description").value(certificateWithId.getDescription()))
        .andExpect(jsonPath("$.price").value(certificateWithId.getPrice()))
        .andExpect(jsonPath("$.duration").value(certificateWithId.getDuration()));
  }

  @Test
  void updateCertificatePutPositiveStatusCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    certificateDao.create(certificate);
    Certificate certificateUpdate = givenNewCertificateForUpdatePutId1();

    mockMvc
        .perform(
            put("/certificates/{id}", certificate.getId())
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void updateCertificatePutNegativeStatusCheck() throws Exception {
    Certificate certificateUpdate = givenNewCertificateForUpdateId1();

    mockMvc
        .perform(
            put("/certificates/{id}", certificateUpdate.getId())
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateCertificatePutPositiveValueCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    certificateDao.create(certificate);
    Certificate certificateUpdate = givenNewCertificateForUpdatePutId1();

    mockMvc
        .perform(
            put("/certificates/{id}", certificate.getId())
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(new ObjectMapper().writeValueAsString(certificateUpdate)));
  }

  @Test
  void updateCertificatePatchPositiveStatusCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    certificateDao.create(certificate);
    Certificate certificateUpdate = givenNewCertificateForUpdateId1();

    mockMvc
        .perform(
            patch("/certificates/{id}", certificate.getId())
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void updateCertificatePatchNegativeStatusCheck() throws Exception {
    Certificate certificateUpdate = givenNewCertificateForUpdateId1();

    mockMvc
        .perform(
            patch("/certificates/{id}", certificateUpdate.getId())
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateCertificatePatchPositiveValueCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    certificateDao.create(certificate);
    Certificate certificateUpdate = givenNewCertificateForUpdateId1();

    mockMvc
        .perform(
            patch("/certificates/{id}", certificate.getId())
                .content(new ObjectMapper().writeValueAsString(certificateUpdate))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(new ObjectMapper().writeValueAsString(certificateUpdate)));
  }

  @Test
  void deleteCertificateStatusCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    certificateDao.create(certificate);

    mockMvc
        .perform(delete("/certificates/{id}", certificate.getId()))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteCertificateValueCheck() throws Exception {
    Certificate certificate = givenExistingCertificate1();
    certificateDao.create(certificate);

    mockMvc.perform(delete("/certificates/{id}", certificate.getId()));
    mockMvc
        .perform(get("/certificates/{id}", certificate.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteCertificateNegative() throws Exception {
    Certificate certificate = givenExistingCertificate1();

    mockMvc
        .perform(delete("/certificates/{id}", certificate.getId()))
        .andExpect(status().isBadRequest());
  }

  private static Certificate givenExistingCertificate1() {
    return Certificate.builder()
        .id(1L)
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 30, 10))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }

  private static Certificate givenExistingCertificate2() {
    return Certificate.builder()
        .id(2L)
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static Certificate givenNewCertificateForUpdatePutId1() {
    return Certificate.builder()
        .id(1L)
        .name("new name")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }

  private static Certificate givenNewCertificateForUpdateId1() {
    return Certificate.builder().id(1L).name("new name").build();
  }

  private static Tag givenExistingTag1() {
    return Tag.builder().id(1L).name("first tag").build();
  }

  private static Tag givenExistingTag2() {
    return Tag.builder().id(2L).name("second tag").build();
  }
}
