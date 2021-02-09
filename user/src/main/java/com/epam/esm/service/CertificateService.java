package com.epam.esm.service;

import com.epam.esm.dto.CertificateWithTagsDto;

/** The interface Certificate service. */
public interface CertificateService {

  /**
   * Persist certificate with tags.
   *
   * @param certificate the certificate
   * @return saved certificate with tags
   */
  CertificateWithTagsDto create(CertificateWithTagsDto certificate);
}
