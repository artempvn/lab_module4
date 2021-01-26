package com.epam.esm.dto;

import com.epam.esm.entity.Certificate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDtoWithTags {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @NotBlank
  @Size(min = 1, max = 45)
  private String name;

  @NotBlank
  @Size(min = 1, max = 1000)
  private String description;

  @NotNull @PositiveOrZero private Double price;

  @NotNull @PositiveOrZero private Integer duration;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime createDate;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime lastUpdateDate;

  private List<TagDto> tags;

  public CertificateDtoWithTags() {}

  public CertificateDtoWithTags(Certificate entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.description = entity.getDescription();
    this.price = entity.getPrice();
    this.duration = entity.getDuration();
    this.createDate = entity.getCreateDate();
    this.lastUpdateDate = entity.getLastUpdateDate();
    this.tags = entity.getTags().stream().map(TagDto::new).collect(Collectors.toList());
  }

  private CertificateDtoWithTags(Builder builder) {
    id = builder.id;
    name = builder.name;
    description = builder.description;
    price = builder.price;
    duration = builder.duration;
    createDate = builder.createDate;
    lastUpdateDate = builder.lastUpdateDate;
    tags = builder.tags;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public List<TagDto> getTags() {
    return tags;
  }

  public void setTags(List<TagDto> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CertificateDtoWithTags that = (CertificateDtoWithTags) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    if (price != null ? !price.equals(that.price) : that.price != null) return false;
    if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
    if (createDate != null
        ? createDate.toEpochSecond(ZoneOffset.UTC) != that.createDate.toEpochSecond(ZoneOffset.UTC)
        : that.createDate != null) return false;
    if (lastUpdateDate != null
        ? lastUpdateDate.toEpochSecond(ZoneOffset.UTC)
            != that.lastUpdateDate.toEpochSecond(ZoneOffset.UTC)
        : that.lastUpdateDate != null) return false;
    return tags != null ? tags.equals(that.tags) : that.tags == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (price != null ? price.hashCode() : 0);
    result = 31 * result + (duration != null ? duration.hashCode() : 0);
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
    result = 31 * result + (tags != null ? tags.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CertificateDtoWithTags{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", price=").append(price);
    sb.append(", duration=").append(duration);
    sb.append(", createDate=").append(createDate);
    sb.append(", lastUpdateDate=").append(lastUpdateDate);
    sb.append(", tags=").append(tags);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<TagDto> tags = Collections.emptyList();

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder price(Double price) {
      this.price = price;
      return this;
    }

    public Builder duration(Integer duration) {
      this.duration = duration;
      return this;
    }

    public Builder createDate(LocalDateTime createDate) {
      this.createDate = createDate;
      return this;
    }

    public Builder lastUpdateDate(LocalDateTime lastUpdateDate) {
      this.lastUpdateDate = lastUpdateDate;
      return this;
    }

    public Builder tags(List<TagDto> tags) {
      this.tags = tags;
      return this;
    }

    public CertificateDtoWithTags build() {
      return new CertificateDtoWithTags(this);
    }
  }
}
