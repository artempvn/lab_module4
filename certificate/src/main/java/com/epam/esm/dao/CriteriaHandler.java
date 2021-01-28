package com.epam.esm.dao;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dto.CertificatesRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;

public interface CriteriaHandler {

    CriteriaUpdate<Certificate> updateWithNotNullFields(
            CriteriaBuilder builder, Certificate certificate);

    CriteriaQuery<Certificate> filterWithParameters(
            CriteriaBuilder builder, CertificatesRequest request);
}
