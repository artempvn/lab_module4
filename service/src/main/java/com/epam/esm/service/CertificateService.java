package com.epam.esm.service;



import com.epam.esm.entity.Certificate;
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
  Certificate create(Certificate certificate);

  /**
   * Read certificate.
   *
   * @param id the id
   * @return the certificate
   */
  Certificate read(long id);

  /**
   * Read all list.
   *
   * @param parameter the parameter
   * @return the list
   */
  List<Certificate> readAll(CertificatesRequest parameter);

  /**
   * Update put certificate.
   *
   * @param certificate the certificate
   * @return the certificate
   */
  Certificate updatePut(Certificate certificate);

  /**
   * Update patch certificate.
   *
   * @param certificate the certificate
   * @return the certificate
   */
  Certificate updatePatch(Certificate certificate);

  /**
   * Delete.
   *
   * @param id the id
   */
  void delete(long id);
}
