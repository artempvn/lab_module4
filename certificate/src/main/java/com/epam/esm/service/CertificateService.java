package com.epam.esm.service;

import com.epam.esm.dto.CertificateWithTagsDto;
import com.epam.esm.dto.CertificateWithoutTagsDto;
import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;

/** The interface Certificate service. */
public interface CertificateService {

  /**
   * Persist certificate with tags.
   *
   * @param certificate the certificate
   * @return saved certificate with tags
   */
  CertificateWithTagsDto create(CertificateWithTagsDto certificate);

  /**
   * Read certificate by id.
   *
   * @param id the id of certificate
   * @return found certificate
   * @throws ResourceNotFoundException if there is no certificate with such id
   */
  CertificateWithTagsDto read(long id);

  /**
   * Read all certificates that meet the request and parameter of pagination.
   *
   * @param request the request contains sorting and filtering staff
   * @param parameter the parameter of pagination
   * @return the page data with found certificates and page info
   */
  PageData<CertificateWithoutTagsDto> readAll(
      CertificatesRequest request, PaginationParameter parameter);

  /**
   * Update existing certificate with new data from input certificate.
   *
   * @param certificate the certificate for replacing existing certificate with same id
   * @return updated certificate
   * @throws ResourceValidationException if there is no certificate with such id
   */
  CertificateWithTagsDto update(CertificateWithTagsDto certificate);

  /**
   * Update only presented fields of input certificate to existing certificate.
   *
   * @param certificate the certificate with fields for updating existing certificate
   * @return updated data of certificate
   * @throws ResourceValidationException if there is no certificate with such id
   */
  CertificateWithoutTagsDto updatePresentedFields(CertificateWithoutTagsDto certificate);

  /**
   * Delete existing certificate by id.
   *
   * @param id the id of certificate
   * @throws ResourceValidationException if there is no certificate with such id
   */
  void delete(long id);
}
