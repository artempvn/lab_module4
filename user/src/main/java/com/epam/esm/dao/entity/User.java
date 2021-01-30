package com.epam.esm.dao.entity;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserDtoWithOrders;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String name;

  @Column private String surname;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL})
  private List<Order> orders;

  public User() {}

  public User(UserDtoWithOrders dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.surname = dto.getSurname();
  }

  private User(Builder builder) {
    id = builder.id;
    name = builder.name;
    surname = builder.surname;
    orders = builder.orders;
  }

  public User(UserDto dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.surname = dto.getSurname();
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

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (id != null ? !id.equals(user.id) : user.id != null) return false;
    if (name != null ? !name.equals(user.name) : user.name != null) return false;
    if (surname != null ? !surname.equals(user.surname) : user.surname != null) return false;
    return orders != null ? orders.equals(user.orders) : user.orders == null;
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
    final StringBuilder sb = new StringBuilder("User{");
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
    private List<Order> orders = Collections.emptyList();

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

    public Builder orders(List<Order> orders) {
      this.orders = orders;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}
