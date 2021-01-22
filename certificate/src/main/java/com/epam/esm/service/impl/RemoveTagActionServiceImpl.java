package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.TagAction;
import com.epam.esm.exception.ResourcesValidationException;
import com.epam.esm.service.TagActionService;
import org.springframework.stereotype.Service;

@Service
public class RemoveTagActionServiceImpl implements TagActionService {
  private final TagDao tagDao;
  private final CertificateDao certificateDao;

  public RemoveTagActionServiceImpl(TagDao tagDao, CertificateDao certificateDao) {
    this.tagDao = tagDao;
    this.certificateDao = certificateDao;
  }

  @Override
  public boolean isApplicable(TagAction tagAction) {
    return tagAction.getType().equals(TagAction.ActionType.REMOVE);
  }

  @Override
  public void processAction(TagAction tagAction) {
    long tagId = tagAction.getTagId();
    long certificateId = tagAction.getCertificateId();
    int numberOfRemovedRows = certificateDao.removeTag(tagId, certificateId);
    if (numberOfRemovedRows == 0) {
      throw ResourcesValidationException.withIds(tagId, certificateId).get();
    }
  }
}
