package com.epam.esm.dto;

import com.epam.esm.dao.entity.User;

public class UserDto {

  private Long id;
  private String name;
  private String surname;

  public UserDto() {}

  public UserDto(User entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.surname = entity.getSurname();
  }

  public UserDto(Long id, String name, String surname) {
    this.id = id;
    this.name = name;
    this.surname = surname;
  }

  private UserDto(Builder builder) {
    id = builder.id;
    name = builder.name;
    surname = builder.surname;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserDto userDto = (UserDto) o;

    if (id != null ? !id.equals(userDto.id) : userDto.id != null) return false;
    if (name != null ? !name.equals(userDto.name) : userDto.name != null) return false;
    return surname != null ? surname.equals(userDto.surname) : userDto.surname == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (surname != null ? surname.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("UserDto{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", surname='").append(surname).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private String name;
    private String surname;

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

    public UserDto build() {
      return new UserDto(this);
    }
  }
}
