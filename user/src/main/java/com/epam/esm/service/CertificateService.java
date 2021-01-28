package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;

import java.security.cert.Certificate;

public interface CertificateService {

    CertificateDto create (CertificateDto certificate);
}
