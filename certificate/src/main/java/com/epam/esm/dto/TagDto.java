package com.epam.esm.dto;

import com.epam.esm.dao.entity.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @NotBlank
  @Size(min = 1, max = 45)
  private String name;

  public TagDto() {}

  public TagDto(Tag tag) {
    this.id = tag.getId();
    this.name = tag.getName();
  }

  private TagDto(Builder builder) {
    id = builder.id;
    name = builder.name;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TagDto tagDto = (TagDto) o;

    if (id != null ? !id.equals(tagDto.id) : tagDto.id != null) return false;
    return name != null ? name.equals(tagDto.name) : tagDto.name == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("TagDto{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private String name;

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public TagDto build() {
      return new TagDto(this);
    }
  }
}
