package com.epam.esm.dto;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateDto {

    private Long id;

    private Long previousId;

    private Double price;

    private List<TagDto> tags;

    private OrderDtoFull order;

    public CertificateDto() {
    }

    public CertificateDto(Certificate entity) {
        this.id = entity.getId();
        this.previousId = entity.getPreviousId();
        this.price = entity.getPrice();
        this.order=new OrderDtoFull(entity.getOrder());
        this.tags = entity.getTags().stream().map(TagDto::new).collect(Collectors.toList());
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

    public OrderDtoFull getOrder() {
        return order;
    }

    public void setOrder(OrderDtoFull order) {
        this.order = order;
    }
}
