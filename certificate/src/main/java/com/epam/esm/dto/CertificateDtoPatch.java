package com.epam.esm.dto;

import com.epam.esm.validator.NullOrNotBlankFieldAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.PositiveOrZero;

public class CertificateDtoPatch {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @NullOrNotBlankFieldAnnotation(message = "{validation.certificate.name}")
  private String name;

  @NullOrNotBlankFieldAnnotation(message = "{validation.certificate.description}")
  private String description;

  @PositiveOrZero private Double price;
  @PositiveOrZero private Integer duration;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CertificateDtoPatch that = (CertificateDtoPatch) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    if (price != null ? !price.equals(that.price) : that.price != null) return false;
    return duration != null ? duration.equals(that.duration) : that.duration == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (price != null ? price.hashCode() : 0);
    result = 31 * result + (duration != null ? duration.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CertificateDtoPatch{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", price=").append(price);
    sb.append(", duration=").append(duration);
    sb.append('}');
    return sb.toString();
  }
}
