package com.epam.esm.web.rest;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.web.advice.ResourceAdvice;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("user")
@AutoConfigureTestDatabase
@SpringBootTest
class OrderControllerTest {

    MockMvc mockMvc;
    @Autowired
    UserDao userDao;
    @Autowired
    OrderController orderController;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    ReloadableResourceBundleMessageSource messageSource;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc =
                MockMvcBuilders.standaloneSetup(orderController)
                        .setControllerAdvice(new ResourceAdvice(messageSource))
                        .build();
    }

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
    void createOrderValueCheck() throws Exception {
        OrderDtoFull order=givenOrder();
        UserDtoWithoutOrders user=givenUser();
        Long userId= userDao.create(user).getId();

        mockMvc
                .perform(
                        post("/users/{id}/orders",userId)
                                .content(new ObjectMapper().writeValueAsString(order))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(status().isCreated());
    }

    OrderDtoFull givenOrder() {
        OrderDtoFull order=new OrderDtoFull();
        var certificate=givenCertificate();
        order.setCertificates(List.of(certificate));
        var user=givenUser();
        order.setUser(user);
        return order;
    }

    UserDtoWithoutOrders givenUser(){
        UserDtoWithoutOrders user=new UserDtoWithoutOrders();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        return user;
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