package com.epam.esm.entity;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserWithOrdersDto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

  @Column(name = "foreign_id")
  private String foreignId;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL})
  private List<Order> orders = Collections.emptyList();

  public User() {}

  public User(UserWithOrdersDto dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.surname = dto.getSurname();
  }

  public User(UserDto dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.surname = dto.getSurname();
  }

  private User(Builder builder) {
    id = builder.id;
    name = builder.name;
    surname = builder.surname;
    foreignId = builder.foreignId;
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

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  public String getForeignId() {
    return foreignId;
  }

  public void setForeignId(String keycloakId) {
    this.foreignId = keycloakId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (id != null ? !id.equals(user.id) : user.id != null) return false;
    if (name != null ? !name.equals(user.name) : user.name != null) return false;
    if (surname != null ? !surname.equals(user.surname) : user.surname != null) return false;
    if (foreignId != null ? !foreignId.equals(user.foreignId) : user.foreignId != null)
      return false;
    return orders != null ? orders.equals(user.orders) : user.orders == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (surname != null ? surname.hashCode() : 0);
    result = 31 * result + (foreignId != null ? foreignId.hashCode() : 0);
    result = 31 * result + (orders != null ? orders.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", surname='").append(surname).append('\'');
    sb.append(", foreignId='").append(foreignId).append('\'');
    sb.append(", orders=").append(orders);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private String name;
    private String surname;
    private String foreignId;
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

    public Builder foreignId(String foreignId) {
      this.foreignId = foreignId;
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
