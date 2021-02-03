package com.epam.esm.dao;

import com.epam.esm.dto.PaginationParameter;

import javax.persistence.TypedQuery;

/** The interface Pagination handler. */
public interface PaginationHandler {

  /**
   * Sets page to query.
   *
   * @param typedQuery the typed query
   * @param parameter the parameter
   */
  void setPageToQuery(TypedQuery<?> typedQuery, PaginationParameter parameter);

  long calculateNumberOfPages(long numberOfElements, int pageSize);
}
