package com.epam.esm.entity;

public class SortParam {
  public enum SortingType {
    ASC,
    DESC;
  }

  private SortingType byDate;
  private SortingType byName;

  public SortingType getByDate() {
    return byDate;
  }

  public void setByDate(SortingType byDate) {
    this.byDate = byDate;
  }

  public SortingType getByName() {
    return byName;
  }

  public void setByName(SortingType byName) {
    this.byName = byName;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SortParam{");
    sb.append("date=").append(byDate);
    sb.append(", name=").append(byName);
    sb.append('}');
    return sb.toString();
  }
}
