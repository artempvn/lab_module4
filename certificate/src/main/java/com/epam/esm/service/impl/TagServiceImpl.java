package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ResourceIsBoundException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagActionService;
import com.epam.esm.service.TagService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TagServiceImpl implements TagService {

  private final TagDao tagDao;
  private final List<TagActionService> tagActionServices;

  public TagServiceImpl(TagDao tagDao, List<TagActionService> tagActionServices) {
    this.tagDao = tagDao;
    this.tagActionServices = tagActionServices;
  }

  @Override
  public TagDto create(TagDto inputTag) {
    Optional<TagDto> existingTag = tagDao.read(inputTag.getName());
    return existingTag.orElseGet(() -> tagDao.create(inputTag));
  }

  @Override
  public TagDto read(long id) {
    Optional<TagDto> tag = tagDao.read(id);
    return tag.orElseThrow(ResourceNotFoundException.notFoundWithTagId(id));
  }

  @Override
  public PageData<TagDto> readAll(PaginationParameter parameter) {
    return tagDao.readAll(parameter);
  }

  @Override
  public void delete(long id) {
    try {
      tagDao.delete(id);
    } catch (DataIntegrityViolationException ex) {
      throw ResourceIsBoundException.isBound(id).get();
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
