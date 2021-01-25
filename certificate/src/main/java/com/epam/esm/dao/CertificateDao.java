package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDtoWithTags;
import com.epam.esm.entity.CertificateDtoWithoutTags;
import com.epam.esm.entity.CertificatesRequest;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {

  CertificateDtoWithTags create(CertificateDtoWithTags certificate);

  Optional<CertificateDtoWithTags> read(long id);

  List<CertificateDtoWithoutTags> readAll(CertificatesRequest request);

  void update(CertificateDtoWithTags certificate);

  void delete(long id);

  void addTag(long tagId, long certificateId);

  int removeTag(long tagId, long certificateId);

  void updatePresentedFields(CertificateDtoWithoutTags certificate);
}
