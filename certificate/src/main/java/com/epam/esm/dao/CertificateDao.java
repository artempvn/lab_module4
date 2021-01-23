package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {

  Certificate create(Certificate certificate);

  Optional<Certificate> read(long id);

  List<Certificate> readAll();

  void update(Certificate certificate);

  void delete(long id);

  void addTag(long tagId, long certificateId);

  int removeTag(long tagId, long certificateId);

  int updatePatch(Certificate certificate);
}
