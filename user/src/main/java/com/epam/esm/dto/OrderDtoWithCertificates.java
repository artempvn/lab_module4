package com.epam.esm.dto;

import com.epam.esm.dao.entity.Order;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDtoWithCertificates {

  private Long id;
  private LocalDateTime createDate;
  private Double price;
  private List<CertificateDto> certificates;

  public OrderDtoWithCertificates() {}

  public OrderDtoWithCertificates(Order entity) {
    this.id = entity.getId();
    this.createDate = entity.getCreateDate();
    this.price = entity.getPrice();
    this.certificates =
        entity.getCertificates().stream().map(CertificateDto::new).collect(Collectors.toList());
  }

  private OrderDtoWithCertificates(Builder builder) {
    id = builder.id;
    createDate = builder.createDate;
    price = builder.price;
    certificates = builder.certificates;
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

  public List<CertificateDto> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateDto> certificates) {
    this.certificates = certificates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OrderDtoWithCertificates that = (OrderDtoWithCertificates) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null)
      return false;
    if (price != null ? !price.equals(that.price) : that.price != null) return false;
    return certificates != null
        ? certificates.equals(that.certificates)
        : that.certificates == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + (price != null ? price.hashCode() : 0);
    result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OrderDtoFull{");
    sb.append("id=").append(id);
    sb.append(", createDate=").append(createDate);
    sb.append(", price=").append(price);
    sb.append(", certificates=").append(certificates);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private LocalDateTime createDate;
    private Double price;
    private List<CertificateDto> certificates = Collections.emptyList();

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

    public Builder certificates(List<CertificateDto> certificates) {
      this.certificates = certificates;
      return this;
    }

    public OrderDtoWithCertificates build() {
      return new OrderDtoWithCertificates(this);
    }
  }
}
