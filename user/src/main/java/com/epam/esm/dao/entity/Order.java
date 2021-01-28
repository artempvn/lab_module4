package com.epam.esm.dao.entity;

import com.epam.esm.dto.OrderDtoFull;

import javax.persistence.*;
import java.time.LocalDateTime;
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

  @OneToMany
          (mappedBy = "order",
                  fetch = FetchType.LAZY,
                  cascade = {CascadeType.ALL})
  private List<Certificate> certificates;

  public Order() {
  }

  public Order(OrderDtoFull dto) {
    this.id = dto.getId();
    this.createDate = dto.getCreateDate();
    this.user = new User(dto.getUser());
    this.certificates = dto.getCertificates().stream().map(Certificate::new).collect(Collectors.toList());
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
}
