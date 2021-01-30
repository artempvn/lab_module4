package com.epam.esm.dao;

import com.epam.esm.dto.CertificateDtoWithTags;

public interface CertificateDao {

  CertificateDtoWithTags create(CertificateDtoWithTags certificate);
}
