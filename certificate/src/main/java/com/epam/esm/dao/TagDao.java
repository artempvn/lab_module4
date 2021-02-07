package com.epam.esm.dao;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.dto.TagDto;
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
  TagDto create(TagDto tag);

  /**
   * Read optional.
   *
   * @param id the id
   * @return the optional
   */
  Optional<TagDto> read(long id);

  /**
   * Read all page data.
   *
   * @param parameter the parameter of pagination
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
   * Read optional.
   *
   * @param name the name
   * @return the optional
   */
  Optional<TagDto> read(String name);
}
