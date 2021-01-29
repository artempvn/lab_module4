package com.epam.esm.dao.entity;

import com.epam.esm.dto.CertificateDtoFull;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "ordered_certificates")
public class Certificate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "previous_id")
  private Long previousId;

  @Column private Double price;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "ordered_certificates_tags",
      joinColumns = {@JoinColumn(name = "certificate_id")},
      inverseJoinColumns = {@JoinColumn(name = "tag_id")})
  private List<Tag> tags;

  public Certificate() {}

  public Certificate(CertificateDtoFull dto) {
    this.id = dto.getId();
    this.previousId = dto.getPreviousId();
    this.price = dto.getPrice();
    this.tags = dto.getTags().stream().map(Tag::new).collect(Collectors.toList());
  }

  private Certificate(Builder builder) {
    id = builder.id;
    previousId = builder.previousId;
    price = builder.price;
    order = builder.order;
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

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Certificate that = (Certificate) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (previousId != null ? !previousId.equals(that.previousId) : that.previousId != null)
      return false;
    if (price != null ? !price.equals(that.price) : that.price != null) return false;
    if (order != null ? !order.equals(that.order) : that.order != null) return false;
    return tags != null ? tags.equals(that.tags) : that.tags == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (previousId != null ? previousId.hashCode() : 0);
    result = 31 * result + (price != null ? price.hashCode() : 0);
    result = 31 * result + (order != null ? order.hashCode() : 0);
    result = 31 * result + (tags != null ? tags.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Certificate{");
    sb.append("id=").append(id);
    sb.append(", previousId=").append(previousId);
    sb.append(", price=").append(price);
    sb.append(", order=").append(order);
    sb.append(", tags=").append(tags);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private Long previousId;
    private Double price;
    private Order order;
    private List<Tag> tags = Collections.emptyList();

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

    public Builder order(Order order) {
      this.order = order;
      return this;
    }

    public Builder tags(List<Tag> tags) {
      this.tags = tags;
      return this;
    }

    public Certificate build() {
      return new Certificate(this);
    }
  }
}
