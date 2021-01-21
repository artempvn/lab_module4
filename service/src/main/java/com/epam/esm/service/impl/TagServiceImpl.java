package com.epam.esm.service.impl;


import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagAction;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.service.TagActionService;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
  public static final int ONE_UPDATED_ROW = 1;

  private final TagDao tagDao;
  private final CertificateDao certificateDao;
  private final List<TagActionService> tagActionServices;

  public TagServiceImpl(
      TagDao tagDao, CertificateDao certificateDao, List<TagActionService> tagActionServices) {
    this.tagDao = tagDao;
    this.certificateDao = certificateDao;
    this.tagActionServices = tagActionServices;
  }

  @Override
  public Tag create(Tag inputTag) {
    Optional<Tag> existingTag = tagDao.read(inputTag.getName());
    return existingTag.orElseGet(() -> tagDao.create(inputTag));
  }

  @Override
  public Tag read(long id) {
    Optional<Tag> tag = tagDao.read(id);
    return tag.orElseThrow(ResourceNotFoundException.notFoundWithTagId(id));
  }

  @Override
  public List<Tag> readAll() {
    return tagDao.readAll();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void delete(long id) {
    certificateDao.deleteCertificateTagsByTagId(id);
    int numberOfUpdatedRows = tagDao.delete(id);
    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
      throw ResourceValidationException.validationWithTagId(id).get();
    }
  }

  @Override
  public void processTagAction(TagAction action) {
    tagActionServices.stream()
        .filter(service -> service.isApplicable(action))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException("There is no valid service to deal with given action"))
        .processAction(action);
  }
}
