package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;

public class PageData<T> extends RepresentationModel<PageData<T>> {
  private long currentPage;
  private long numberOfElements;
  private long numberOfPages;
  private Collection<T> content;

  public PageData() {}

  public PageData(
      long currentPage, long numberOfElements, long numberOfPages, Collection<T> content) {
    this.currentPage = currentPage;
    this.numberOfElements = numberOfElements;
    this.numberOfPages = numberOfPages;
    this.content = content;
  }

  public long getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(long currentPage) {
    this.currentPage = currentPage;
  }

  public long getNumberOfPages() {
    return numberOfPages;
  }

  public void setNumberOfPages(long numberOfPages) {
    this.numberOfPages = numberOfPages;
  }

  public long getNumberOfElements() {
    return numberOfElements;
  }

  public void setNumberOfElements(long numberOfElements) {
    this.numberOfElements = numberOfElements;
  }

  public Collection<T> getContent() {
    return content;
  }

  public void setContent(Collection<T> content) {
    this.content = content;
  }
}
