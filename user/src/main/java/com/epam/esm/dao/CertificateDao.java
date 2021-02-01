package com.epam.esm.dao;

import com.epam.esm.dto.CertificateDtoWithTags;

/** The interface Certificate dao. */
public interface CertificateDao {

  /**
   * Create certificate dto with tags.
   *
   * @param certificate the certificate
   * @return the certificate dto with tags
   */
  CertificateDtoWithTags create(CertificateDtoWithTags certificate);
}
