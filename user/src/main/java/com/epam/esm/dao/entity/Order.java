package com.epam.esm.dao.entity;

import com.epam.esm.dto.OrderDtoWithCertificatesWithTagsForCreation;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "create_date")
  private LocalDateTime createDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;


  @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
  private List<Certificate> certificates;


  @Formula(
      "( Select SUM(ordered_certificates.price) FROM ordered_certificates WHERE ordered_certificates.order_id=id)")
  private double price;

  public Order() {}

  public Order(OrderDtoWithCertificatesWithTagsForCreation dto) {
    this.id = dto.getId();
    this.createDate = dto.getCreateDate();
    this.user = User.builder().id(dto.getUserId()).build();
    this.certificates =
        dto.getCertificates().stream().map(Certificate::new).collect(Collectors.toList());
  }

  private Order(Builder builder) {
    id = builder.id;
    createDate = builder.createDate;
    user = builder.user;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Certificate> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<Certificate> certificates) {
    this.certificates = certificates;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Order order = (Order) o;

    if (id != null ? !id.equals(order.id) : order.id != null) return false;
    if (createDate != null ? !createDate.equals(order.createDate) : order.createDate != null)
      return false;
    if (user != null ? !user.equals(order.user) : order.user != null) return false;
    return certificates != null
        ? certificates.equals(order.certificates)
        : order.certificates == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + (user != null ? user.hashCode() : 0);
    result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Order{");
    sb.append("id=").append(id);
    sb.append(", createDate=").append(createDate);
    sb.append(", user=").append(user);
    sb.append(", certificates=").append(certificates);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Long id;
    private LocalDateTime createDate;
    private User user;
    private List<Certificate> certificates = Collections.emptyList();

    private Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder createDate(LocalDateTime createDate) {
      this.createDate = createDate;
      return this;
    }

    public Builder user(User user) {
      this.user = user;
      return this;
    }

    public Builder certificates(List<Certificate> certificates) {
      this.certificates = certificates;
      return this;
    }

    public Order build() {
      return new Order(this);
    }
  }
}
