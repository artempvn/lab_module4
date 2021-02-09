package com.epam.esm.service;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ResourceIsBoundException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;

/** The interface Tag service. */
public interface TagService {

  /**
   * Persist tag. If it is already exist (by name) returns existing tag, otherwise persist new one.
   *
   * @param tag the tag
   * @return saved tag or existing tag, if tag has already been persisted
   */
  TagDto create(TagDto tag);

  /**
   * Read tag by id.
   *
   * @param id the id of tag
   * @return found tag
   * @throws ResourceNotFoundException if there is no tag with such id
   */
  TagDto read(long id);

  /**
   * Read all tags that meet parameter of pagination
   *
   * @param parameter the parameter of pagination
   * @return the page data with found tags and page info
   */
  PageData<TagDto> readAll(PaginationParameter parameter);

  /**
   * Delete existing tag by id.
   *
   * @param id the id of tag
   * @throws ResourceValidationException if there is no tag with such id
   * @throws ResourceIsBoundException if tag is bound to any certificate
   */
  void delete(long id);

  /**
   * Process tag action.
   *
   * @param action the action
   */
  void processTagAction(TagAction action);
}
