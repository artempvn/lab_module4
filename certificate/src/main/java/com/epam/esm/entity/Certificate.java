package com.epam.esm.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "gift_certificates")
public class Certificate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String name;

  @Column private String description;

  @Column private Double price;

  @Column private Integer duration;

  @Column(name = "create_date")
  private LocalDateTime createDate;

  @Column(name = "last_update_date")
  private LocalDateTime lastUpdateDate;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH})
  @JoinTable(
      name = "certificates_tags",
      joinColumns = {@JoinColumn(name = "certificate_id")},
      inverseJoinColumns = {@JoinColumn(name = "tag_id")})
  private List<Tag> tags;

  public Certificate() {}

  public Certificate(CertificateDtoWithTags dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.description = dto.getDescription();
    this.price = dto.getPrice();
    this.duration = dto.getDuration();
    this.createDate = dto.getCreateDate();
    this.lastUpdateDate = dto.getLastUpdateDate();
    this.tags = dto.getTags().stream().map(Tag::new).collect(Collectors.toList());
  }

  public Certificate(CertificateDtoWithoutTags dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.description = dto.getDescription();
    this.price = dto.getPrice();
    this.duration = dto.getDuration();
    this.createDate = dto.getCreateDate();
    this.lastUpdateDate = dto.getLastUpdateDate();
  }

  private Certificate(Builder builder) {
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

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Certificate{");
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Certificate that = (Certificate) o;

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

  public static class Builder {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<Tag> tags = Collections.emptyList();

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

    public Builder tags(List<Tag> tags) {
      this.tags = tags;
      return this;
    }

    public Certificate build() {
      return new Certificate(this);
    }
  }
}
