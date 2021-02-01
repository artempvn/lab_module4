package com.epam.esm.web.rest;

import com.epam.esm.dto.*;
import com.epam.esm.service.CertificateService;
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

  public CertificateController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<CertificateDtoWithTags> readCertificate(@PathVariable long id) {
    CertificateDtoWithTags certificate = certificateService.read(id);
    Link selfLink = linkTo(CertificateController.class).slash(id).withSelfRel();
    certificate.add(selfLink);
    certificate
        .getTags()
        .forEach(tag -> tag.add(linkTo(TagController.class).slash(tag.getId()).withSelfRel()));
    return ResponseEntity.status(HttpStatus.OK).body(certificate);
  }

  @GetMapping
  public List<CertificateDtoWithoutTags> readCertificates(
      CertificatesRequest request, @Valid PaginationParameter parameter) {
    List<CertificateDtoWithoutTags> certificates = certificateService.readAll(request, parameter);
    certificates.forEach(
        certificate ->
            certificate.add(
                linkTo(CertificateController.class).slash(certificate.getId()).withSelfRel()));

    return certificates;
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
}
