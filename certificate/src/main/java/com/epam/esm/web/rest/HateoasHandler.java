package com.epam.esm.web.rest;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

/** The interface Hateoas handler. */
public interface HateoasHandler {
  /**
   * Build links for pagination list.
   *
   * @param clazz the clazz
   * @param parameter the parameter
   * @param numberOfPages the number of pages
   * @return the list
   */
  List<Link> buildLinksForPagination(
      Class<?> clazz, PaginationParameter parameter, long numberOfPages);

  /**
   * Wrap page with entity model entity model.
   *
   * @param <T> the type parameter
   * @param page the page
   * @return the entity model
   */
  <T> EntityModel<PageData<EntityModel<T>>> wrapPageWithEntityModel(PageData<T> page);
}
