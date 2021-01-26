package com.epam.esm.service;

import com.epam.esm.entity.TagAction;
import com.epam.esm.entity.TagDto;

import java.util.List;

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
   * Read all list.
   *
   * @return the list
   */
  List<TagDto> readAll();

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
