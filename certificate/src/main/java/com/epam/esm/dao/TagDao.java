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
   * Persist tag.
   *
   * @param tag the tag
   * @return saved tag
   */
  Tag create(Tag tag);

  /**
   * Read tag by id.
   *
   * @param id the id of tag
   * @return the optional of tag or empty optional if it's not exist
   */
  Optional<Tag> read(long id);

  /**
   * Read all tags that meet parameter of pagination.
   *
   * @param parameter the parameter of pagination
   * @return the page data with found tags and page info
   */
  PageData<Tag> readAll(PaginationParameter parameter);

  /**
   * Delete existing tag by id.
   *
   * @param id the id of tag
   */
  void delete(long id);

  /**
   * Read tag by name.
   *
   * @param name the name of tag
   * @return the optional of tag or empty optional if it's not exist
   */
  Optional<Tag> read(String name);
}
