package com.epam.esm.dto;

import com.epam.esm.dao.entity.Order;

import java.time.LocalDateTime;

public class OrderDto {

  private Long id;
  private LocalDateTime createDate;
  private Double price;

  public OrderDto() {}

  public OrderDto(Order entity) {
    this.id = entity.getId();
    this.createDate = entity.getCreateDate();
    this.price = entity.getPrice();
  }

  private OrderDto(Builder builder) {
    id = builder.id;
    createDate = builder.createDate;
    price = builder.price;
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

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OrderDto orderDto = (OrderDto) o;

    if (id != null ? !id.equals(orderDto.id) : orderDto.id != null) return false;
    if (createDate != null ? !createDate.equals(orderDto.createDate) : orderDto.createDate != null)
      return false;
    return price != null ? price.equals(orderDto.price) : orderDto.price == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + (price != null ? price.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OrderDto{");
    sb.append("id=").append(id);
    sb.append(", createDate=").append(createDate);
    sb.append(", price=").append(price);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private LocalDateTime createDate;
    private Double price;

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder createDate(LocalDateTime createDate) {
      this.createDate = createDate;
      return this;
    }

    public Builder price(Double price) {
      this.price = price;
      return this;
    }

    public OrderDto build() {
      return new OrderDto(this);
    }
  }
}
