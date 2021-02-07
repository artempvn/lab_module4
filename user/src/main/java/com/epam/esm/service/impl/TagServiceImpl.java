package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

  private final TagDao tagDao;

  public TagServiceImpl(TagDao tagDao) {
    this.tagDao = tagDao;
  }

  @Override
  public TagDto create(TagDto tag) {
    Optional<TagDto> existingTag = tagDao.read(tag.getName());
    return existingTag.orElseGet(() -> tagDao.create(tag));
  }
}
