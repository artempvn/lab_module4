package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@SpringBootTest
class TagDaoImplTest {

  public static final long ID_FOR_3_TAG = 3L;
  public static final int NOT_EXISTED_TAG_ID = 99999;
  @Autowired TagDao tagDao;

  @BeforeEach
  void setUp() {
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);
  }

  @Test
  void create() {
    Tag expectedTag = givenNewTagWithoutId();

    Tag actualTag = tagDao.create(expectedTag);

    assertEquals(expectedTag, actualTag);
  }

  @Test
  void read1() {
    Tag expectedTag = givenExistingTag1();

    Optional<Tag> actualTag = tagDao.read(expectedTag.getId());

    assertEquals(Optional.of(expectedTag), actualTag);
  }

  @Test
  void read2() {
    Optional<Tag> expectedTag = Optional.empty();

    Optional<Tag> actualTag = tagDao.read(NOT_EXISTED_TAG_ID);

    assertEquals(expectedTag, actualTag);
  }

  @Test
  void readAll() {
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    List<Tag> expectedList = List.of(tag1, tag2);

    List<Tag> actualList = tagDao.readAll();
    assertEquals(expectedList, actualList);
  }

  @Test
  void delete() {
    Tag tag = givenExistingTag1();

    tagDao.delete(tag.getId());

    assertTrue(tagDao.read(tag.getId()).isEmpty());
  }

  @ParameterizedTest
  @MethodSource("readTagByNameDataProvider")
  void readTest(Tag wantedTag, Optional<Tag> expectedTag) {
    Optional<Tag> actualTag = tagDao.read(wantedTag.getName());
    assertEquals(expectedTag, actualTag);
  }

  static Stream<Arguments> readTagByNameDataProvider() {
    return Stream.of(
        arguments(givenExistingTag1(), Optional.of(givenExistingTag1())),
        arguments(givenNewTagWithoutId(), Optional.empty()));
  }

  private static Tag givenExistingTag1() {
    return Tag.builder().id(1L).name("first tag").build();
  }

  private static Tag givenExistingTag2() {
    return Tag.builder().id(2L).name("second tag").build();
  }

  private static Tag givenNewTagWithoutId() {
    return Tag.builder().name("third tag").build();
  }
}
