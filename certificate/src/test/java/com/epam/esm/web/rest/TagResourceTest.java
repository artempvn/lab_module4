package com.epam.esm.web.rest;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.TagAction;
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

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("certificate")
@AutoConfigureTestDatabase
@SpringBootTest
class TagResourceTest {
  public static final long NOT_EXISTING_ID = 99999L;
  MockMvc mockMvc;
  @Autowired TagDao tagDao;
  @Autowired CertificateDao certificateDao;
  @Autowired TagResource tagController;
  @Autowired EntityManager entityManager;
  @Autowired ReloadableResourceBundleMessageSource messageSource;
  @Autowired TransactionTemplate txTemplate;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(tagController)
            .setControllerAdvice(new ResourceAdvice(messageSource))
            .build();
  }

  @AfterEach
  void setDown() {
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
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

    mockMvc.perform(get("/tags/{id}", NOT_EXISTING_ID)).andExpect(status().isNotFound());
  }

  @Test
  void readTagsStatusCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();
    TagDto tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);

    mockMvc.perform(get("/tags?page=1&size=10")).andExpect(status().isOk());
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
        .perform(get("/tags?page=1&size=10"))
        .andExpect(jsonPath("$.currentPage").value(1))
        .andExpect(jsonPath("$.content").isNotEmpty())
        .andExpect(
            jsonPath(
                "$.links[?(@.rel=='self')].href",
                contains("http://localhost/tags?page=1&size=10")));
  }

  @Test
  void createTagStatusCheck() throws Exception {
    TagDto tag1 = givenExistingTag1();

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
    tag1.setId(NOT_EXISTING_ID);
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

    mockMvc.perform(delete("/tags/{id}", NOT_EXISTING_ID)).andExpect(status().isBadRequest());
  }

  private static TagDto givenExistingTag1() {
    return TagDto.builder().name("first tag").build();
  }

  private static TagDto givenExistingTag2() {
    return TagDto.builder().name("second tag").build();
  }

  private static CertificateDtoWithTags givenExistingCertificate1() {
    return CertificateDtoWithTags.builder()
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .build();
  }
}
