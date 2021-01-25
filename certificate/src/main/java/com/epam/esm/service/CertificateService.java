package com.epam.esm.service;

import com.epam.esm.entity.CertificateDtoWithTags;
import com.epam.esm.entity.CertificateDtoWithoutTags;
import com.epam.esm.entity.CertificatesRequest;

import java.util.List;

/** The interface Certificate service. */
public interface CertificateService {

  /**
   * Create certificate.
   *
   * @param certificate the certificate
   * @return the certificate
   */
  CertificateDtoWithTags create(CertificateDtoWithTags certificate);

  /**
   * Read certificate.
   *
   * @param id the id
   * @return the certificate
   */
  CertificateDtoWithTags read(long id);

  List<CertificateDtoWithoutTags> readAll(CertificatesRequest request);

  /**
   * Update put certificate.
   *
   * @param certificate the certificate
   * @return the certificate
   */
  CertificateDtoWithTags updatePut(CertificateDtoWithTags certificate);

  /**
   * Update patch certificate.
   *
   * @param certificate the certificate
   * @return the certificate
   */
  CertificateDtoWithoutTags updatePatch(CertificateDtoWithoutTags certificate);

  /**
   * Delete.
   *
   * @param id the id
   */
  void delete(long id);
}
