package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/** The interface Tag dao. */
public interface TagDao {

  /**
   * Create tag dto.
   *
   * @param tag the tag
   * @return the tag dto
   */
  Tag create(Tag tag);

  /**
   * Read optional.
   *
   * @param name the name
   * @return the optional
   */
  Optional<Tag> read(String name);
}
