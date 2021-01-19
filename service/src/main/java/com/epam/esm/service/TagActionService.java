package com.epam.esm.service;

import com.epam.esm.entity.TagAction;

/** The interface Tag action service. */
public interface TagActionService {
  /**
   * Is applicable boolean.
   *
   * @param tagAction the tag action
   * @return the boolean
   */
  boolean isApplicable(TagAction tagAction);

  /**
   * Process action.
   *
   * @param tagAction the tag action
   */
  void processAction(TagAction tagAction);
}
