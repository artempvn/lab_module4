package com.epam.esm.dto;

import com.epam.esm.dao.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDtoWithOrders {

  private Long id;
  private String name;
  private String surname;
  private List<OrderDto> orders;

  public UserDtoWithOrders() {}

  public UserDtoWithOrders(User entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.surname = entity.getSurname();
    this.orders = entity.getOrders().stream().map(OrderDto::new).collect(Collectors.toList());
  }

  private UserDtoWithOrders(Builder builder) {
    id = builder.id;
    name = builder.name;
    surname = builder.surname;
    orders = builder.orders;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public List<OrderDto> getOrders() {
    return orders;
  }

  public void setOrders(List<OrderDto> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserDtoWithOrders that = (UserDtoWithOrders) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
    return orders != null ? orders.equals(that.orders) : that.orders == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (surname != null ? surname.hashCode() : 0);
    result = 31 * result + (orders != null ? orders.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("UserDtoFull{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", surname='").append(surname).append('\'');
    sb.append(", orders=").append(orders);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private String name;
    private String surname;
    private List<OrderDto> orders = Collections.emptyList();

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder surname(String surname) {
      this.surname = surname;
      return this;
    }

    public Builder orders(List<OrderDto> orders) {
      this.orders = orders;
      return this;
    }

    public UserDtoWithOrders build() {
      return new UserDtoWithOrders(this);
    }
  }
}
