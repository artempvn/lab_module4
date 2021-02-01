package com.epam.esm.service;

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
}
