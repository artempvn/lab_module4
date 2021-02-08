package com.epam.esm.web.rest;

import com.epam.esm.dto.*;
import com.epam.esm.service.CertificateService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/** The type Certificate resource. */
@RestController
@RequestMapping("/certificates")
public class CertificateResource {

  private final CertificateService certificateService;
  private final HateoasHandler hateoasHandler;

  /**
   * Instantiates a new Certificate resource.
   *
   * @param certificateService the certificate service
   * @param hateoasHandler the hateoas handler
   */
  public CertificateResource(CertificateService certificateService, HateoasHandler hateoasHandler) {
    this.certificateService = certificateService;
    this.hateoasHandler = hateoasHandler;
  }

  /**
   * Read certificate response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<CertificateWithTagsDto>> readCertificate(
      @PathVariable long id) {
    EntityModel<CertificateWithTagsDto> certificate = EntityModel.of(certificateService.read(id));
    certificate.add(buildCertificateLinks(certificate.getContent().getId()));
    return ResponseEntity.status(HttpStatus.OK).body(certificate);
  }

  /**
   * Read certificates response entity.
   *
   * @param request the request contains sorting and filtering staff
   * @param parameter the parameter of pagination
   * @return the response entity
   */
  @GetMapping
  public ResponseEntity<EntityModel<PageData<EntityModel<CertificateWithoutTagsDto>>>>
      readCertificates(CertificatesRequest request, @Valid PaginationParameter parameter) {
    PageData<CertificateWithoutTagsDto> page = certificateService.readAll(request, parameter);

    EntityModel<PageData<EntityModel<CertificateWithoutTagsDto>>> hateoasPage =
        hateoasHandler.wrapPageWithEntityModel(page);

    for (EntityModel<CertificateWithoutTagsDto> certificate :
        hateoasPage.getContent().getContent()) {
      long id = certificate.getContent().getId();
      List<Link> links = buildCertificateLinks(id);
      certificate.add(links);
    }

    hateoasPage.add(
        hateoasHandler.buildLinksForPagination(
            CertificateResource.class, parameter, page.getNumberOfPages(), request));
    hateoasPage.add(buildCertificatesLinks());
    return ResponseEntity.status(HttpStatus.OK).body(hateoasPage);
  }

  /**
   * Create certificate response entity.
   *
   * @param certificate the certificate
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<CertificateWithTagsDto> createCertificate(
      @Valid @RequestBody CertificateWithTagsDto certificate) {
    CertificateWithTagsDto createdCertificate = certificateService.create(certificate);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCertificate);
  }

  /**
   * Update certificate put response entity.
   *
   * @param id the id
   * @param certificate the certificate
   * @return the response entity
   */
  @PutMapping("/{id}")
  public ResponseEntity<CertificateWithTagsDto> updateCertificatePut(
      @PathVariable long id, @Valid @RequestBody CertificateWithTagsDto certificate) {
    certificate.setId(id);
    CertificateWithTagsDto updatedCertificate = certificateService.update(certificate);
    return ResponseEntity.status(HttpStatus.OK).body(updatedCertificate);
  }

  /**
   * Update certificate patch response entity.
   *
   * @param id the id
   * @param certificate the certificate
   * @return the response entity
   */
  @PatchMapping("/{id}")
  public ResponseEntity<CertificateWithoutTagsDto> updateCertificatePatch(
      @PathVariable long id, @Valid @RequestBody CertificatePatchDto certificate) {
    certificate.setId(id);
    CertificateWithoutTagsDto updatedCertificate =
        certificateService.updatePresentedFields(new CertificateWithoutTagsDto(certificate));
    return ResponseEntity.status(HttpStatus.OK).body(updatedCertificate);
  }

  /**
   * Delete certificate.
   *
   * @param id the id
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCertificate(@PathVariable long id) {
    certificateService.delete(id);
  }

  /**
   * Build certificate links list.
   *
   * @param id the id
   * @return the list
   */
  List<Link> buildCertificateLinks(long id) {
    return List.of(
        linkTo(CertificateResource.class).slash(id).withSelfRel(),
        linkTo(CertificateResource.class).slash(id).withRel("put").withName("update certificate"),
        linkTo(CertificateResource.class)
            .slash(id)
            .withRel("patch")
            .withName("update certificate's fields"),
        linkTo(CertificateResource.class)
            .slash(id)
            .withRel("delete")
            .withName("delete certificate"));
  }

  /**
   * Build certificates links list.
   *
   * @return the list
   */
  List<Link> buildCertificatesLinks() {
    return List.of(
        linkTo(CertificateResource.class).withRel("post").withName("create certificate"));
  }
}
