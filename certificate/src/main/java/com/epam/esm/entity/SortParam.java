package com.epam.esm.entity;

public class SortParam {
  public enum SortingType {
    ASC,
    DESC;
  }

  private SortingType date;
  private SortingType name;

  public SortingType getDate() {
    return date;
  }

  public void setDate(SortingType date) {
    this.date = date;
  }

  public SortingType getName() {
    return name;
  }

  public void setName(SortingType name) {
    this.name = name;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SortParam{");
    sb.append("date=").append(date);
    sb.append(", name=").append(name);
    sb.append('}');
    return sb.toString();
  }
}
