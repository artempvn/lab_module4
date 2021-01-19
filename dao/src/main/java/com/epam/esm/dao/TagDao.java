package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/** The interface Tag dao. */
public interface TagDao {

  /**
   * Create tag.
   *
   * @param tag the tag
   * @return the tag
   */
  Tag create(Tag tag);

  /**
   * Read optional.
   *
   * @param id the id
   * @return the optional
   */
  Optional<Tag> read(long id);

  /**
   * Read all list.
   *
   * @return the list
   */
  List<Tag> readAll();

  /**
   * Delete int.
   *
   * @param id the id
   * @return the int
   */
  int delete(long id);

  /**
   * Read optional.
   *
   * @param name the name
   * @return the optional
   */
  Optional<Tag> read(String name);
}
