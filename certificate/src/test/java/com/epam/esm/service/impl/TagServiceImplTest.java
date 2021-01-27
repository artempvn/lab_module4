package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.entity.TagAction;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ResourceIsBoundException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceValidationException;
import com.epam.esm.exception.ResourcesValidationException;
import com.epam.esm.service.TagActionService;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

  public static final long TAG_ID = 1L;
  public static final long CERTIFICATE_ID = 1L;
  public static final int ONE_DELETED_ROW = 1;
  public static final int NO_DELETED_ROW = 0;
  TagDao tagDao = mock(TagDao.class);
  CertificateDao certificateDao = mock(CertificateDao.class);
  TagActionService addTagService = new AddTagActionServiceImpl(tagDao, certificateDao);
  TagActionService removeTagService = new RemoveTagActionServiceImpl(certificateDao);
  TagService tagService = new TagServiceImpl(tagDao, List.of(addTagService, removeTagService));

  @Test
  void createTagDaoReadInvocation() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao).read(tag.getName());
  }

  @Test
  void createIfExistedTagDaoCreateInvocation() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao, never()).create(any());
  }

  @Test
  void createIfExistedTagDaoReadInvocation() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao).read(tag.getName());
  }

  @Test
  void createIfNotExistedTagDaoCreateInvocation() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.readAll()).thenReturn(Collections.emptyList());
    when(tagDao.read(tag.getName())).thenReturn(Optional.empty());
    when(tagDao.create(tag)).thenReturn(new TagDto());

    tagService.create(tag);

    verify(tagDao).create(tag);
  }

  @Test
  void read() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));

    tagService.read(TAG_ID);

    verify(tagDao).read(anyLong());
  }

  @Test
  void readException() {
    when(tagDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> tagService.read(TAG_ID));
  }

  @Test
  void readAll() {
    tagService.readAll();

    verify(tagDao).readAll();
  }

  @Test
  void deleteTagDaoDeleteInvocation() {
    tagService.delete(TAG_ID);

    verify(tagDao).delete(TAG_ID);
  }

  @Test
  void deleteTagDaoDeleteBoundException() {
    doThrow(JpaSystemException.class).when(tagDao).delete(anyLong());

    assertThrows(ResourceIsBoundException.class, () -> tagService.delete(TAG_ID));
  }

  @Test
  void processTagActionAddTagDaoReadInvocation() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    CertificateDtoWithTags certificate =
        CertificateDtoWithTags.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    tagService.processTagAction(tagAction);

    verify(tagDao).read(tag.getId());
  }

  @Test
  void processTagActionAddCertificateDaoReadInvocation() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    CertificateDtoWithTags certificate =
        CertificateDtoWithTags.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    tagService.processTagAction(tagAction);

    verify(certificateDao).read(certificate.getId());
  }

  @Test
  void processTagActionAddCertificateDaoAddTagInvocation() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    CertificateDtoWithTags certificate =
        CertificateDtoWithTags.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    tagService.processTagAction(tagAction);

    verify(certificateDao).addTag(tag.getId(), certificate.getId());
  }

  @Test
  void processTagActionAddTagDaoReadException() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    CertificateDtoWithTags certificate =
        CertificateDtoWithTags.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.empty());
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    assertThrows(ResourceValidationException.class, () -> tagService.processTagAction(tagAction));
  }

  @Test
  void processTagActionAddCertificateDaoReadException() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));
    when(certificateDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceValidationException.class, () -> tagService.processTagAction(tagAction));
  }

  @Test
  void processTagActionRemoveCertificateDaoRemoveTagInvocation() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction =
        new TagAction(TagAction.ActionType.REMOVE, certificate.getId(), tag.getId());
    when(certificateDao.removeTag(anyLong(), anyLong())).thenReturn(ONE_DELETED_ROW);

    tagService.processTagAction(tagAction);

    verify(certificateDao).removeTag(tag.getId(), certificate.getId());
  }

  @Test
  void processTagActionRemoveCertificateDaoRemoveTagException() {
    TagDto tag = TagDto.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction =
        new TagAction(TagAction.ActionType.REMOVE, certificate.getId(), tag.getId());
    when(certificateDao.removeTag(anyLong(), anyLong())).thenReturn(NO_DELETED_ROW);

    assertThrows(ResourcesValidationException.class, () -> tagService.processTagAction(tagAction));
  }
}
