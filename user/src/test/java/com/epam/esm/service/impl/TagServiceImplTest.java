package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceImplTest {
  TagDao tagDao = mock(TagDao.class);
  TagService tagService = new TagServiceImpl(tagDao);

  @Test
  void createTagDaoReadInvocation() {
    TagDto tag = givenTag();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao).read(tag.getName());
  }

  @Test
  void createIfExistedTagDaoCreateInvocation() {
    TagDto tag = givenTag();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao, never()).create(any());
  }

  @Test
  void createIfExistedTagDaoReadInvocation() {
    TagDto tag = givenTag();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao).read(tag.getName());
  }

  @Test
  void createIfNotExistedTagDaoCreateInvocation() {
    TagDto tag = givenTag();
    when(tagDao.read(tag.getName())).thenReturn(Optional.empty());
    when(tagDao.create(tag)).thenReturn(new TagDto());

    tagService.create(tag);

    verify(tagDao).create(tag);
  }

  TagDto givenTag() {
    TagDto tag = new TagDto();
    tag.setName("tag name");
    return tag;
  }
}
