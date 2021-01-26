package com.epam.esm.service;

import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.CertificateDtoWithoutTags;
import com.epam.esm.entity.CertificatesRequest;

import java.util.List;

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
   * Read all list.
   *
   * @param request the request
   * @return the list
   */
  List<CertificateDtoWithoutTags> readAll(CertificatesRequest request);

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
