package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.TagAction;
import com.epam.esm.exception.ResourceException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.TagActionService;
import org.springframework.stereotype.Service;

@Service
public class AddTagActionServiceImpl implements TagActionService {
  private final TagDao tagDao;
  private final CertificateDao certificateDao;

  public AddTagActionServiceImpl(TagDao tagDao, CertificateDao certificateDao) {
    this.tagDao = tagDao;
    this.certificateDao = certificateDao;
  }

  @Override
  public boolean isApplicable(TagAction tagAction) {
    return tagAction.getType().equals(TagAction.ActionType.ADD);
  }

  @Override
  public void processAction(TagAction tagAction) {
    long tagId = tagAction.getTagId();
    long certificateId = tagAction.getCertificateId();
    if (tagDao.read(tagId).isEmpty()) {
      throw ResourceValidationException.validationWithTagId(tagId).get();
    }
    if (certificateDao.read(certificateId).isEmpty()) {
      throw ResourceValidationException.validationWithCertificateId(certificateId).get();
    }
    certificateDao.addTag(tagId, certificateId);
  }
}
