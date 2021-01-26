package com.epam.esm.dao;

import com.epam.esm.entity.TagDto;
import org.springframework.stereotype.Repository;

import java.util.List;
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
   * Read optional.
   *
   * @param name the name
   * @return the optional
   */
  Optional<TagDto> read(String name);
}
