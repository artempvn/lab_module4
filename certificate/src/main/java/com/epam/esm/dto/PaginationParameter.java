package com.epam.esm.dto;

import javax.validation.constraints.Positive;

public class PaginationParameter {

  @Positive private int page;
  @Positive private int size;

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PaginationParameter{");
    sb.append("page=").append(page);
    sb.append(", size=").append(size);
    sb.append('}');
    return sb.toString();
  }
}
