package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;

/** The interface Certificate dao. */
public interface CertificateDao {

  /**
   * Persist certificate with binding all of its tags.
   *
   * @param certificate the certificate with tags
   * @return saved certificate
   */
  Certificate create(Certificate certificate);
}
