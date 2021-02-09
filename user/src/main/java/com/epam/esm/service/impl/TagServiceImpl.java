package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

  private final TagDao tagDao;

  public TagServiceImpl(TagDao tagDao) {
    this.tagDao = tagDao;
  }

  @Override
  public TagDto create(TagDto inputTag) {
    Optional<Tag> existingTag = tagDao.read(inputTag.getName());
    if (existingTag.isEmpty()) {
      Tag tag = tagDao.create(new Tag(inputTag));
      return new TagDto(tag);
    }
    return existingTag.map(TagDto::new).orElseThrow();
  }
}
