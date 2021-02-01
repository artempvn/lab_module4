package com.epam.esm.service;

import com.epam.esm.dto.CertificateDtoWithTags;

/** The interface Certificate service. */
public interface CertificateService {

  /**
   * Create certificate dto with tags.
   *
   * @param certificate the certificate
   * @return the certificate dto with tags
   */
  CertificateDtoWithTags create(CertificateDtoWithTags certificate);
}
