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
  CertificateDtoWithTags create(CertificateDtoWithTags certificate);

  /**
   * Read certificate dto with tags.
   *
   * @param id the id
   * @return the certificate dto with tags
   */
  CertificateDtoWithTags read(long id);

  /**
   * Read all page data.
   *
   * @param request the request contains sorting and filtering staff
   * @param parameter the parameter of pagination
   * @return the page data
   */
  PageData<CertificateDtoWithoutTags> readAll(
      CertificatesRequest request, PaginationParameter parameter);

  /**
   * Update certificate dto with tags.
   *
   * @param certificate the certificate
   * @return the certificate dto with tags
   */
  CertificateDtoWithTags update(CertificateDtoWithTags certificate);

  /**
   * Update presented fields certificate dto without tags.
   *
   * @param certificate the certificate
   * @return the certificate dto without tags
   */
  CertificateDtoWithoutTags updatePresentedFields(CertificateDtoWithoutTags certificate);

  /**
   * Delete.
   *
   * @param id the id
   */
  void delete(long id);
}
