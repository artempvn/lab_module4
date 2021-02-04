package com.epam.esm.dto;

import com.epam.esm.dao.entity.Certificate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateDtoWithTags {

  private Long id;
  @NotNull private Long previousId;
  @NotNull @PositiveOrZero private Double price;
  @Valid private List<TagDto> tags = Collections.emptyList();

  public CertificateDtoWithTags() {}

  public CertificateDtoWithTags(Certificate entity) {
    this.id = entity.getId();
    this.previousId = entity.getPreviousId();
    this.price = entity.getPrice();
    this.tags = entity.getTags().stream().map(TagDto::new).collect(Collectors.toList());
  }

  private CertificateDtoWithTags(Builder builder) {
    id = builder.id;
    previousId = builder.previousId;
    price = builder.price;
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

  public Long getPreviousId() {
    return previousId;
  }

  public void setPreviousId(Long previousId) {
    this.previousId = previousId;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
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
    if (previousId != null ? !previousId.equals(that.previousId) : that.previousId != null)
      return false;
    if (price != null ? !price.equals(that.price) : that.price != null) return false;
    return tags != null ? tags.equals(that.tags) : that.tags == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (previousId != null ? previousId.hashCode() : 0);
    result = 31 * result + (price != null ? price.hashCode() : 0);
    result = 31 * result + (tags != null ? tags.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CertificateDtoFull{");
    sb.append("id=").append(id);
    sb.append(", previousId=").append(previousId);
    sb.append(", price=").append(price);
    sb.append(", tags=").append(tags);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private Long previousId;
    private Double price;
    private List<TagDto> tags = Collections.emptyList();

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder previousId(Long previousId) {
      this.previousId = previousId;
      return this;
    }

    public Builder price(Double price) {
      this.price = price;
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
