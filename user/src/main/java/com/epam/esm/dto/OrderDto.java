package com.epam.esm.dto;

import com.epam.esm.dao.entity.Order;

import java.time.LocalDateTime;

public class OrderDto {

  private Long id;
  private LocalDateTime createDate;

  public OrderDto() {}

  public OrderDto(Order entity) {
    this.id = entity.getId();
    this.createDate = entity.getCreateDate();
  }

  private OrderDto(Builder builder) {
    id = builder.id;
    createDate = builder.createDate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OrderDto orderDto = (OrderDto) o;

    if (id != null ? !id.equals(orderDto.id) : orderDto.id != null) return false;
    return createDate != null
        ? createDate.equals(orderDto.createDate)
        : orderDto.createDate == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OrderDto{");
    sb.append("id=").append(id);
    sb.append(", createDate=").append(createDate);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private LocalDateTime createDate;

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder createDate(LocalDateTime createDate) {
      this.createDate = createDate;
      return this;
    }

    public OrderDto build() {
      return new OrderDto(this);
    }
  }
}
