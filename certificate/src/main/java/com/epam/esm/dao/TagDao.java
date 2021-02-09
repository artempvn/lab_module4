package com.epam.esm.dao;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** The interface Tag dao. */
@Repository
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
   * @param id the id
   * @return the optional
   */
  Optional<Tag> read(long id);

  /**
   * Read all page data.
   *
   * @param parameter the parameter of pagination
   * @return the page data
   */
  PageData<Tag> readAll(PaginationParameter parameter);

  /**
   * Delete.
   *
   * @param id the id
   */
  void delete(long id);

  /**
   * Read optional.
   *
   * @param name the name
   * @return the optional
   */
  Optional<Tag> read(String name);
}
