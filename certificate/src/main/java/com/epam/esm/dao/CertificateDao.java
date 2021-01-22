package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {

  Certificate create(Certificate certificate);

  Optional<Certificate> read(long id);

  List<Certificate> readAll();

  int update(Certificate certificate);

  int delete(long id);

  void addTag(long tagId, long certificateId);

  int removeTag(long tagId, long certificateId);

  List<Tag> readCertificateTags(long certificateId);

  void deleteCertificateTagsByTagId(long tagId);

  void deleteCertificateTagsByCertificateId(long certificateId);

  int updatePatch(Certificate certificate);
}
