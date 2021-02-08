package com.epam.esm.service;

import com.epam.esm.dto.*;

/** The interface Certificate service. */
public interface CertificateService {

  /**
   * Create certificate dto with tags.
   *
   * @param certificate the certificate
   * @return the certificate dto with tags
   */
  CertificateWithTagsDto create(CertificateWithTagsDto certificate);

  /**
   * Read certificate dto with tags.
   *
   * @param id the id
   * @return the certificate dto with tags
   */
  CertificateWithTagsDto read(long id);

  /**
   * Read all page data.
   *
   * @param request the request contains sorting and filtering staff
   * @param parameter the parameter of pagination
   * @return the page data
   */
  PageData<CertificateWithoutTagsDto> readAll(
      CertificatesRequest request, PaginationParameter parameter);

  /**
   * Update certificate dto with tags.
   *
   * @param certificate the certificate
   * @return the certificate dto with tags
   */
  CertificateWithTagsDto update(CertificateWithTagsDto certificate);

  /**
   * Update presented fields certificate dto without tags.
   *
   * @param certificate the certificate
   * @return the certificate dto without tags
   */
  CertificateWithoutTagsDto updatePresentedFields(CertificateWithoutTagsDto certificate);

  /**
   * Delete.
   *
   * @param id the id
   */
  void delete(long id);
}
