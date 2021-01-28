package com.epam.esm.dao.entity;

import com.epam.esm.dto.TagDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "tags_backup")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    private List<Certificate> certificates = new ArrayList<>();

    public Tag() {
    }

    public Tag(TagDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
    }

    private Tag(Builder builder) {
        id = builder.id;
        name = builder.name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public Tag withCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != null ? !id.equals(tag.id) : tag.id != null) return false;
        if (name != null ? !name.equals(tag.name) : tag.name != null) return false;
        return certificates != null ? certificates.equals(tag.certificates) : tag.certificates == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", certificates=").append(certificates);
        sb.append('}');
        return sb.toString();
    }


    public static class Builder {
        private Long id;
        private String name;
        private List<Certificate> certificates = Collections.emptyList();

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder certificates(List<Certificate> certificates) {
            this.certificates = certificates;
            return this;
        }

        public Tag build() {
            return new Tag(this);
        }
    }
}
