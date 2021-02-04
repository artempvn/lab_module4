package com.epam.esm.service;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagAction;
import com.epam.esm.dto.TagDto;

/** The interface Tag service. */
public interface TagService {

  /**
   * Create tag dto.
   *
   * @param tag the tag
   * @return the tag dto
   */
  TagDto create(TagDto tag);

  /**
   * Read tag dto.
   *
   * @param id the id
   * @return the tag dto
   */
  TagDto read(long id);

  /**
   * Read all page data.
   *
   * @param parameter the parameter
   * @return the page data
   */
  PageData<TagDto> readAll(PaginationParameter parameter);

  /**
   * Delete.
   *
   * @param id the id
   */
  void delete(long id);

  /**
   * Process tag action.
   *
   * @param action the action
   */
  void processTagAction(TagAction action);
}
