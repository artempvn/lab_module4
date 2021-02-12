package com.epam.esm.web.rest;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class TagResourceSecurityTest {

  @Autowired TagDao tagDao;
  @Autowired EntityManager entityManager;
  @Autowired TransactionTemplate txTemplate;
  @Autowired MockMvc mockMvc;

  @AfterEach
  void setDown() {
    String sql = "DELETE FROM CERTIFICATES_TAGS;DELETE FROM tag;DELETE FROM gift_certificates";
    txTemplate.execute(status -> entityManager.createNativeQuery(sql).executeUpdate());
  }

  @Test
  @WithMockUser(roles = "USER")
  void readTagStatusCheckAuth() throws Exception {
    Tag tag = givenExistingTag1();
    long id = tagDao.create(tag).getId();

    mockMvc.perform(get("/tags/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void readTagStatusCheckNoAuth() throws Exception {
    Tag tag = givenExistingTag1();
    long id = tagDao.create(tag).getId();

    mockMvc.perform(get("/tags/{id}", id)).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  void deleteTagStatusCheckUserAuth() throws Exception {
    Tag tag = givenExistingTag1();
    long id = tagDao.create(tag).getId();

    mockMvc.perform(delete("/tags/{id}", id)).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteTagStatusCheckAdminAuth() throws Exception {
    Tag tag = givenExistingTag1();
    long id = tagDao.create(tag).getId();

    mockMvc.perform(delete("/tags/{id}", id)).andExpect(status().isNoContent());
  }

  private static Tag givenExistingTag1() {
    return Tag.builder().name("first tag").build();
  }
}
