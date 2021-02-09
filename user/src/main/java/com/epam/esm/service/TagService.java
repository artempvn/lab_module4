package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

/** The interface Tag service. */
public interface TagService {

  /**
   * Persist tag. If it is already exist (by name) returns existing tag, otherwise persist new one.
   *
   * @param tag the tag
   * @return saved tag or existing tag, if tag has already been persisted
   */
  TagDto create(TagDto tag);
}
