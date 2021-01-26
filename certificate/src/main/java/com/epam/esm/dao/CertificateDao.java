package com.epam.esm.dao;

import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.CertificateDtoWithoutTags;
import com.epam.esm.entity.CertificatesRequest;

import java.util.List;
import java.util.Optional;

/** The interface Certificate dao. */
public interface CertificateDao {

  /**
   * Create certificate dto with tags.
   *
   * @param certificate the certificate
   * @return the certificate dto with tags
   */
  CertificateDtoWithTags create(CertificateDtoWithTags certificate);

  /**
   * Read optional.
   *
   * @param id the id
   * @return the optional
   */
  Optional<CertificateDtoWithTags> read(long id);

  /**
   * Read all list.
   *
   * @param request the request
   * @return the list
   */
  List<CertificateDtoWithoutTags> readAll(CertificatesRequest request);

  /**
   * Update.
   *
   * @param certificate the certificate
   */
  void update(CertificateDtoWithTags certificate);

  /**
   * Delete.
   *
   * @param id the id
   */
  void delete(long id);

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
   * Update presented fields.
   *
   * @param certificate the certificate
   */
  void updatePresentedFields(CertificateDtoWithoutTags certificate);
}
