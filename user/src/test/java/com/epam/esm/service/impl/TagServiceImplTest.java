package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceImplTest {
  TagDao tagDao = mock(TagDao.class);
  TagService tagService = new TagServiceImpl(tagDao);

  @Test
  void createTagDaoReadInvocation() {
    Tag tag = givenTag();
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    tagService.create(new TagDto());

    verify(tagDao).read(any());
  }

  @Test
  void createIfExistedTagDaoCreateInvocation() {
    Tag tag = givenTag();
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    tagService.create(new TagDto());

    verify(tagDao, never()).create(any());
  }

  @Test
  void createIfExistedTagDaoReadInvocation() {
    Tag tag = givenTag();
    when(tagDao.read(any())).thenReturn(Optional.of(tag));

    tagService.create(new TagDto());

    verify(tagDao).read(any());
  }

  @Test
  void createIfNotExistedTagDaoCreateInvocation() {
    Tag tag = givenTag();
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(new Tag());

    tagService.create(new TagDto());

    verify(tagDao).create(any());
  }

  Tag givenTag() {
    Tag tag = new Tag();
    tag.setId(1L);
    tag.setName("tag name");
    return tag;
  }
}
