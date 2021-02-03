package com.epam.esm.web.service;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

public interface HateoasHandler {
  List<Link> takeLinksForPagination(
      Class<?> clazz, PaginationParameter parameter, long numberOfPages);

  <T> EntityModel<PageData<EntityModel<T>>> wrapPageWithEntityModel(PageData<?> page);
}
