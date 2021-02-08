package com.epam.esm.service;

import com.epam.esm.dto.CertificateWithTagsDto;

/** The interface Certificate service. */
public interface CertificateService {

  /**
   * Create certificate dto with tags.
   *
   * @param certificate the certificate
   * @return the certificate dto with tags
   */
  CertificateWithTagsDto create(CertificateWithTagsDto certificate);
}
