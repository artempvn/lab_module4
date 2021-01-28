package com.epam.esm.dto;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDtoFull {

    private Long id;

    private LocalDateTime createDate;

    private UserDtoWithoutOrders user;

    private List<CertificateDto> certificates;

    public OrderDtoFull() {
    }

    public OrderDtoFull(Order entity) {
        this.id = entity.getId();
        this.createDate = entity.getCreateDate();
        this.user = new UserDtoWithoutOrders(entity.getUser());
        this.certificates = entity.getCertificates().stream().map(CertificateDto::new).collect(Collectors.toList());
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

    public UserDtoWithoutOrders getUser() {
        return user;
    }

    public void setUser(UserDtoWithoutOrders user) {
        this.user = user;
    }

    public List<CertificateDto> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateDto> certificates) {
        this.certificates = certificates;
    }
}
