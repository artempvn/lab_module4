package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificatePatch;
import com.epam.esm.entity.CertificatesRequest;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/** The interface Certificate dao. */
public interface CertificateDao {

  /**
   * Create certificate.
   *
   * @param certificate the certificate
   * @return the certificate
   */
  Certificate create(Certificate certificate);

  /**
   * Read optional.
   *
   * @param id the id
   * @return the optional
   */
  Optional<Certificate> read(long id);

  /**
   * Read all list.
   *
   * @param parameter the parameter
   * @return the list
   */
  List<Certificate> readAll(CertificatesRequest parameter);

  /**
   * Update int.
   *
   * @param certificate the certificate
   * @return the int
   */
  int update(Certificate certificate);

  /**
   * Delete int.
   *
   * @param id the id
   * @return the int
   */
  int delete(long id);

  /**
   * Add tag.
   *
   * @param tagId the tag id
   * @param certificateId the certificate id
   */
  void addTag(long tagId, long certificateId);

  /**
   * Remove tag int.
   *
   * @param tagId the tag id
   * @param certificateId the certificate id
   * @return the int
   */
  int removeTag(long tagId, long certificateId);

  /**
   * Read certificate tags list.
   *
   * @param certificateId the certificate id
   * @return the list
   */
  List<Tag> readCertificateTags(long certificateId);

  /**
   * Delete certificate tags by tag id.
   *
   * @param tagId the tag id
   */
  void deleteCertificateTagsByTagId(long tagId);

  /**
   * Delete certificate tags by certificate id.
   *
   * @param certificateId the certificate id
   */
  void deleteCertificateTagsByCertificateId(long certificateId);

  /**
   * Update patch int.
   *
   * @param certificate the certificate
   * @return the int
   */
  int updatePatch(CertificatePatch certificate);
}
