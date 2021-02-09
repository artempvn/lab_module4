package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceIsBoundException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.TagActionService;
import com.epam.esm.service.TagService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    Optional<Tag> existingTag = tagDao.read(inputTag.getName());
    if (existingTag.isEmpty()) {
      Tag tag = tagDao.create(new Tag(inputTag));
      return new TagDto(tag);
    }
    return existingTag.map(TagDto::new).orElseThrow();
  }

  @Override
  public TagDto read(long id) {
    Optional<Tag> tag = tagDao.read(id);
    return tag.map(TagDto::new).orElseThrow(ResourceNotFoundException.notFoundWithTagId(id));
  }

  @Override
  public PageData<TagDto> readAll(PaginationParameter parameter) {
    PageData<Tag> tagPageData = tagDao.readAll(parameter);
    long numberOfElements = tagPageData.getNumberOfElements();
    long numberOfPages = tagPageData.getNumberOfPages();
    List<TagDto> tags =
        tagPageData.getContent().stream().map(TagDto::new).collect(Collectors.toList());
    return new PageData<>(parameter.getPage(), numberOfElements, numberOfPages, tags);
  }

  @Override
  public void delete(long id) {
    Optional<Tag> tag = tagDao.read(id);
    tag.orElseThrow(ResourceValidationException.validationWithTagId(id));
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
