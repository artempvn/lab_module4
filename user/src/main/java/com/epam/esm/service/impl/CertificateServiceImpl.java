package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final TagService tagService;
    private final CertificateDao certificateDao;

    public CertificateServiceImpl(TagService tagService, CertificateDao certificateDao) {
        this.tagService = tagService;
        this.certificateDao = certificateDao;
    }

    @Override
    public CertificateDto create(CertificateDto certificate) {
        certificate.setTags(
                certificate.getTags().stream().map(tagService::create).collect(Collectors.toList()));
        return certificateDao.create(certificate);
    }
}
