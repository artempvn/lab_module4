package com.epam.esm.dao;

import com.epam.esm.dto.CertificateWithTagsDto;

/** The interface Certificate dao. */
public interface CertificateDao {

  /**
   * Create certificate dto with tags.
   *
   * @param certificate the certificate
   * @return the certificate dto with tags
   */
  CertificateWithTagsDto create(CertificateWithTagsDto certificate);
}
