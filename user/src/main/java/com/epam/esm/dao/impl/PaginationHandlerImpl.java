package com.epam.esm.dao.impl;

import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dto.PaginationParameter;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;

@Component
public class PaginationHandlerImpl implements PaginationHandler {

  @Override
  public void setPageToQuery(TypedQuery<?> typedQuery, PaginationParameter parameter) {
    if (parameter.getPage() != null && parameter.getSize() != null) {
      int page = parameter.getPage();
      int pageSize = parameter.getSize();
      typedQuery.setFirstResult((page - 1) * pageSize);
      typedQuery.setMaxResults(pageSize);
    }
  }
}
