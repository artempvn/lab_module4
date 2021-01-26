package com.epam.esm.controller;

import com.epam.esm.advice.ResourceAdvice;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.entity.TagAction;
import com.epam.esm.dto.TagDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@AutoConfigureTestDatabase
@SpringBootTest
class TagControllerTest {
  MockMvc mockMvc;
  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired TagController tagController;
  @Autowired SessionFactory sessionFactory;
  @Autowired ReloadableResourceBundleMessageSource messageSource;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(tagController)
            .setControllerAdvice(new ResourceAdvice(messageSource))
            .setValidator(null)
            .build();
  }

  @AfterEach
  void setDown() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    session.createNativeQuery(sql).executeUpdate();
    session.getTransaction().commit();
    session.close();
  }

  @Test
  void readTagPositiveStatusCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();
    long tagId = tagDao.create(tag1).getId();

    mockMvc.perform(get("/tags/{id}", tagId)).andExpect(status().isOk());
  }

  @Test
  void readTagPositiveValueCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();
    long id = tagDao.create(tag1).getId();

    mockMvc
        .perform(get("/tags/{id}", id))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value(tag1.getName()));
  }

  @Test
  void readTagNegativeStatusCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();

    mockMvc.perform(get("/tags/{id}", tag1.getId())).andExpect(status().isNotFound());
  }

  @Test
  void readTagsStatusCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();
    TagDto tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);

    mockMvc.perform(get("/tags")).andExpect(status().isOk());
  }

  @Test
  void readTagsValueCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();
    TagDto tag2 = givenExistingTag2();
    long tagId1 = tagDao.create(tag1).getId();
    long tagId2 = tagDao.create(tag2).getId();
    tag1.setId(tagId1);
    tag2.setId(tagId2);

    mockMvc
        .perform(get("/tags"))
        .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(tag1, tag2))));
  }

  @Test
  void createTagStatusCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();
    tag1.setId(null);

    mockMvc
        .perform(
            post("/tags")
                .content(new ObjectMapper().writeValueAsString(tag1))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  void createTagValueCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();
    tag1.setId(null);

    mockMvc
        .perform(
            post("/tags")
                .content(new ObjectMapper().writeValueAsString(tag1))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value(tag1.getName()));
  }

  @Test
  void processTagAction() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    long certificateId = certificateDao.create(certificate1).getId();
    TagDto tag1 = givenExistingTag1();
    long tagId = tagDao.create(tag1).getId();
    certificateDao.addTag(tagId, certificateId);
    TagAction tagAction = new TagAction(TagAction.ActionType.REMOVE, certificateId, tagId);

    mockMvc
        .perform(
            post("/tags/action")
                .content(new ObjectMapper().writeValueAsString(tagAction))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void processTagActionNegative() throws Exception {
    CertificateDtoWithTags certificate1 = givenExistingCertificate1();
    long id = certificateDao.create(certificate1).getId();
    TagDto tag1 = givenExistingTag1();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, id, tag1.getId());

    mockMvc
        .perform(
            post("/tags/action")
                .content(new ObjectMapper().writeValueAsString(tagAction))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteTagStatusCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();
    long tagId = tagDao.create(tag1).getId();

    mockMvc.perform(delete("/tags/{id}", tagId)).andExpect(status().isNoContent());
  }

  @Test
  void deleteTagStatusCheckAfterRequest() throws Exception {
    TagDto tag1 = givenExistingTag1();
    long id = tagDao.create(tag1).getId();

    mockMvc.perform(delete("/tags/{id}", id));

    mockMvc.perform(get("/tags/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void deleteTagNegative() throws Exception {
    TagDto tag1 = givenExistingTag1();

    mockMvc.perform(delete("/tags/{id}", tag1.getId())).andExpect(status().isBadRequest());
  }

  private static TagDto givenExistingTag1() {
    return TagDto.builder().id(1L).name("first tag").build();
  }

  private static TagDto givenExistingTag2() {
    return TagDto.builder().id(2L).name("second tag").build();
  }

  private static CertificateDtoWithTags givenExistingCertificate1() {
    return CertificateDtoWithTags.builder()
        .id(1L)
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }
}
