package com.epam.esm.dao;

import com.epam.esm.dto.PaginationParameter;

import javax.persistence.TypedQuery;

/** The interface Pagination handler. */
public interface PaginationHandler {

  /**
   * Sets page to query.
   *
   * @param typedQuery the typed query
   * @param parameter the parameter of pagination
   */
  void setPageToQuery(TypedQuery<?> typedQuery, PaginationParameter parameter);

  /**
   * Calculate number of pages by number of elements and the size of page.
   *
   * @param numberOfElements total number of elements
   * @param pageSize the page size
   * @return the long number of pages
   */
  long calculateNumberOfPages(long numberOfElements, int pageSize);
}
