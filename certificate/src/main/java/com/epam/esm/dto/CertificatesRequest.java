package com.epam.esm.dto;

import java.util.Collections;
import java.util.List;

public class CertificatesRequest {

  private List<String> tags= Collections.emptyList();
  private String name;
  private String description;
  private SortParam sort;

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SortParam getSort() {
    return sort;
  }

  public void setSort(SortParam sort) {
    this.sort = sort;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CertificatesRequest{");
    sb.append("tags=").append(tags);
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", sort=").append(sort);
    sb.append('}');
    return sb.toString();
  }
}
