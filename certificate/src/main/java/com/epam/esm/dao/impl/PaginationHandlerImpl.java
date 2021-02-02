package com.epam.esm.dao.impl;

import com.epam.esm.dao.PaginationHandler;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.PaginationParameter;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Component
public class PaginationHandlerImpl implements PaginationHandler {

  @Override
  public void setPageToQuery(TypedQuery<?> typedQuery, PaginationParameter parameter) {
      int page = parameter.getPage();
      int pageSize = parameter.getSize();
      typedQuery.setFirstResult((page - 1) * pageSize);
      typedQuery.setMaxResults(pageSize);
  }

  @Override
  public long calculateNumberOfPages(long numberOfElements, int pageSize){
     return (long) Math.ceil(1.0*numberOfElements/pageSize);
  }


}
