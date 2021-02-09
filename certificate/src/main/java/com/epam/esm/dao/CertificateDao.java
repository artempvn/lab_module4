package com.epam.esm.dao;

import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.dto.PageData;
import com.epam.esm.dto.PaginationParameter;
import com.epam.esm.entity.Certificate;

import java.util.Optional;

/** The interface Certificate dao. */
public interface CertificateDao {

  /**
   * Persist certificate with binding all of its tags
   *
   * @param certificate the certificate with tags
   * @return saved certificate
   */
  Certificate create(Certificate certificate);

  /**
   * Read certificate by id.
   *
   * @param id the id of certificate
   * @return the optional of certificate or empty optional if it's not exist
   */
  Optional<Certificate> read(long id);

  /**
   * Read all certificates that meet the request and parameter of pagination.
   *
   * @param request the request contains sorting and filtering staff
   * @param parameter the parameter of pagination
   * @return the page data with found certificates and page info
   */
  PageData<Certificate> readAll(CertificatesRequest request, PaginationParameter parameter);

  /**
   * Update existing certificate with new data from input certificate.
   *
   * @param certificate the certificate for replacing existing certificate with same id
   */
  void update(Certificate certificate);

  /**
   * Delete existing certificate by id.
   *
   * @param id the id of certificate
   */
  void delete(long id);

  /**
   * Add tag to certificate by their ids.
   *
   * @param tagId the tag id
   * @param certificateId the certificate id
   */
  void addTag(long tagId, long certificateId);

  /**
   * Remove tag from certificate by their ids.
   *
   * @param tagId the tag id
   * @param certificateId the certificate id
   * @return the number of changed rows, 0- if there is no such combination of ids.
   */
  int removeTag(long tagId, long certificateId);

  /**
   * Update only presented fields of input certificate to existing certificate.
   *
   * @param certificate the certificate with fields for updating existing certificate
   */
  void updatePresentedFields(Certificate certificate);
}
