package com.epam.esm.dto;

import com.epam.esm.dao.entity.Certificate;

public class CertificateDto {

  private Long id;
  private Long previousId;
  private Double price;

  private CertificateDto(Builder builder) {
    id = builder.id;
    previousId = builder.previousId;
    price = builder.price;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getId() {
    return id;
  }

  public CertificateDto() {}

  public CertificateDto(Certificate entity) {
    this.id = entity.getId();
    this.previousId = entity.getPreviousId();
    this.price = entity.getPrice();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CertificateDto that = (CertificateDto) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (previousId != null ? !previousId.equals(that.previousId) : that.previousId != null)
      return false;
    return price != null ? price.equals(that.price) : that.price == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (previousId != null ? previousId.hashCode() : 0);
    result = 31 * result + (price != null ? price.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CertificateDto{");
    sb.append("id=").append(id);
    sb.append(", previousId=").append(previousId);
    sb.append(", price=").append(price);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private Long previousId;
    private Double price;

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

    public CertificateDto build() {
      return new CertificateDto(this);
    }
  }
}
