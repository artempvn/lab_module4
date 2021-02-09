package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/** The interface Tag dao. */
public interface TagDao {

  /**
   * Persist tag.
   *
   * @param tag the tag
   * @return saved tag
   */
  Tag create(Tag tag);

  /**
   * Read tag by name.
   *
   * @param name the name of tag
   * @return the optional of tag or empty optional if it's not exist
   */
  Optional<Tag> read(String name);
}
