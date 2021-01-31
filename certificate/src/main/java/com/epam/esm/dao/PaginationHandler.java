package com.epam.esm.dao;

import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dto.PaginationParameter;

import javax.persistence.TypedQuery;

public interface PaginationHandler {

    void setPageToQuery(TypedQuery<?> typedQuery, PaginationParameter parameter);
}
