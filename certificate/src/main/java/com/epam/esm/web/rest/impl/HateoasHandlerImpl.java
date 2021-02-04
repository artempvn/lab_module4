package com.epam.esm.web.rest.impl;

import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.web.rest.HateoasHandler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class HateoasHandlerImpl implements HateoasHandler {

  @Override
  public List<Link> buildLinksForPagination(
      Class<?> clazz, PaginationParameter parameter, long numberOfPages) {
    long currentPage = parameter.getPage();
    List<Link> links = new ArrayList<>();
    links.add(
        linkTo(clazz)
            .slash(String.format("?page=%d&size=%d", currentPage, parameter.getSize()))
            .withSelfRel());

    if (currentPage > 1) {
      links.add(
          linkTo(clazz)
              .slash(String.format("?page=%d&size=%d", currentPage - 1, parameter.getSize()))
              .withRel("previous page"));
    }

    if (currentPage < numberOfPages) {
      links.add(
          linkTo(clazz)
              .slash(String.format("?page=%d&size=%d", currentPage + 1, parameter.getSize()))
              .withRel("next page"));
    }
    return links;
  }

  public <T> EntityModel<PageData<EntityModel<T>>> wrapPageWithEntityModel(PageData<T> page) {
    List<EntityModel<T>> innerList =
        page.getContent().stream().map(EntityModel::of).collect(Collectors.toList());

    return EntityModel.of(
        new PageData<>(
            page.getCurrentPage(), page.getNumberOfElements(), page.getNumberOfPages(), innerList));
  }
}
