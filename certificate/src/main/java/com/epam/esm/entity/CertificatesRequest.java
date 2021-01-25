package com.epam.esm.entity;

public class CertificatesRequest {

  private String tag;
  private String name;
  private String description;
  private SortParam sort;

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
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
    sb.append("tag='").append(tag).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", sort=").append(sort);
    sb.append('}');
    return sb.toString();
  }
}
