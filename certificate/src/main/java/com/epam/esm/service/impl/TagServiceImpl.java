package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagAction;
import com.epam.esm.entity.TagDto;
import com.epam.esm.exception.ResourceException;
import com.epam.esm.service.TagActionService;
import com.epam.esm.service.TagService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

  private final TagDao tagDao;
  private final List<TagActionService> tagActionServices;

  public TagServiceImpl(TagDao tagDao, List<TagActionService> tagActionServices) {
    this.tagDao = tagDao;
    this.tagActionServices = tagActionServices;
  }

  @Override
  public TagDto create(TagDto inputTag) {
    Optional<Tag> existingTag = tagDao.read(inputTag.getName());
    return new TagDto(existingTag.orElseGet(() -> tagDao.create(inputTag)));
  }

  @Override
  public TagDto read(long id) {
    Optional<Tag> tag = tagDao.read(id);
    return new TagDto(tag.orElseThrow(ResourceException.notFoundWithTagId(id)));
  }

  @Override
  public List<TagDto> readAll() {
    return tagDao.readAll().stream().map(TagDto::new).collect(Collectors.toList());
  }

  @Override
  public void delete(long id) {
    try {
      tagDao.delete(id);
    } catch (DataIntegrityViolationException ex) {
      throw ResourceException.isBound(id).get();
    }
  }

  @Override
  public void processTagAction(TagAction action) {
    tagActionServices.stream()
        .filter(service -> service.isApplicable(action))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException("There is no valid service to deal with given action"))
        .processAction(action);
  }
}
