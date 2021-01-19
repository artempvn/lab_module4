package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagAction;

import java.util.List;

/** The interface Tag service. */
public interface TagService {

  /**
   * Create tag.
   *
   * @param tag the tag
   * @return the tag
   */
  Tag create(Tag tag);

  /**
   * Read tag.
   *
   * @param id the id
   * @return the tag
   */
  Tag read(long id);

  /**
   * Read all list.
   *
   * @return the list
   */
  List<Tag> readAll();

  /**
   * Delete.
   *
   * @param id the id
   */
  void delete(long id);

  /**
   * Process tag action.
   *
   * @param action the action
   */
  void processTagAction(TagAction action);
}
