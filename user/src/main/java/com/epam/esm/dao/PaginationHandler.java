package com.epam.esm.dao;

import com.epam.esm.dto.PaginationParameter;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;

public interface PaginationHandler {

    void setPageToQuery(TypedQuery<?> typedQuery, PaginationParameter parameter);

}
