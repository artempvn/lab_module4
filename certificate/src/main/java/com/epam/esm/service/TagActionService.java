package com.epam.esm.service;

import com.epam.esm.dto.TagAction;

/** The interface Tag action service. */
public interface TagActionService {
  /**
   * Is applicable boolean.
   *
   * @param tagAction the tag action
   * @return is applicable tag action or not
   */
  boolean isApplicable(TagAction tagAction);

  /**
   * Process action of tag action.
   *
   * @param tagAction the tag action
   */
  void processAction(TagAction tagAction);
}
