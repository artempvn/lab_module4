package com.epam.esm.dao;

import com.epam.esm.dto.TagDto;

import java.util.Optional;

/** The interface Tag dao. */
public interface TagDao {

  /**
   * Create tag dto.
   *
   * @param tag the tag
   * @return the tag dto
   */
  TagDto create(TagDto tag);

  /**
   * Read optional.
   *
   * @param name the name
   * @return the optional
   */
  Optional<TagDto> read(String name);
}
