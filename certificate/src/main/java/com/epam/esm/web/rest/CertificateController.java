package com.epam.esm.web.rest;

import com.epam.esm.dto.*;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.service.HateoasHandler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

  private final CertificateService certificateService;
  private final HateoasHandler hateoasHandler;

  public CertificateController(
      CertificateService certificateService, HateoasHandler hateoasHandler) {
    this.certificateService = certificateService;
    this.hateoasHandler = hateoasHandler;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<CertificateDtoWithTags>> readCertificate(
      @PathVariable long id) {
    EntityModel<CertificateDtoWithTags> certificate = EntityModel.of(certificateService.read(id));
    certificate.add(takeCertificateLinks(certificate.getContent().getId()));
    return ResponseEntity.status(HttpStatus.OK).body(certificate);
  }

  @GetMapping
  public ResponseEntity<EntityModel<PageData<EntityModel<CertificateDtoWithoutTags>>>>
      readCertificates(CertificatesRequest request, @Valid PaginationParameter parameter) {
    PageData<CertificateDtoWithoutTags> page = certificateService.readAll(request, parameter);

    EntityModel<PageData<EntityModel<CertificateDtoWithoutTags>>> hateoasPage =
        hateoasHandler.wrapPageWithEntityModel(page);
    hateoasPage
        .getContent()
        .getContent()
        .forEach(
            certificate -> certificate.add(takeCertificateLinks(certificate.getContent().getId())));

    hateoasPage.add(
        hateoasHandler.takeLinksForPagination(
            CertificateController.class, parameter, page.getNumberOfPages()));
    hateoasPage.add(takeCertificatesLinks());
    return ResponseEntity.status(HttpStatus.OK).body(hateoasPage);
  }

  @PostMapping
  public ResponseEntity<CertificateDtoWithTags> createCertificate(
      @Valid @RequestBody CertificateDtoWithTags certificate) {
    CertificateDtoWithTags createdCertificate = certificateService.create(certificate);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCertificate);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CertificateDtoWithTags> updateCertificatePut(
      @PathVariable long id, @Valid @RequestBody CertificateDtoWithTags certificate) {
    certificate.setId(id);
    CertificateDtoWithTags updatedCertificate = certificateService.update(certificate);
    return ResponseEntity.status(HttpStatus.OK).body(updatedCertificate);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CertificateDtoWithoutTags> updateCertificatePatch(
      @PathVariable long id, @Valid @RequestBody CertificateDtoPatch certificate) {
    certificate.setId(id);
    CertificateDtoWithoutTags updatedCertificate =
        certificateService.updatePresentedFields(new CertificateDtoWithoutTags(certificate));
    return ResponseEntity.status(HttpStatus.OK).body(updatedCertificate);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCertificate(@PathVariable long id) {
    certificateService.delete(id);
  }

  List<Link> takeCertificateLinks(long id) {
    return List.of(
        linkTo(CertificateController.class).slash(id).withSelfRel(),
        linkTo(CertificateController.class).slash(id).withRel("put certificate"),
        linkTo(CertificateController.class).slash(id).withRel("patch certificate"),
        linkTo(CertificateController.class).slash(id).withRel("delete certificate"));
  }

  List<Link> takeCertificatesLinks() {
    return List.of(linkTo(CertificateController.class).withRel("create certificate"));
  }
}
