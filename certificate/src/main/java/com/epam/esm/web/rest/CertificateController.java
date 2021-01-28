package com.epam.esm.web.rest;

import com.epam.esm.dto.CertificateDtoPatch;
import com.epam.esm.dto.CertificateDtoWithTags;
import com.epam.esm.dto.CertificateDtoWithoutTags;
import com.epam.esm.dto.CertificatesRequest;
import com.epam.esm.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/** The type Certificate controller. */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

  private final CertificateService certificateService;

  /**
   * Instantiates a new Certificate controller.
   *
   * @param certificateService the certificate service
   */
  public CertificateController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  /**
   * Read certificate response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @GetMapping("/{id}")
  public ResponseEntity<CertificateDtoWithTags> readCertificate(@PathVariable long id) {
    CertificateDtoWithTags certificate = certificateService.read(id);
    return ResponseEntity.status(HttpStatus.OK).body(certificate);
  }

  /**
   * Read certificates list.
   *
   * @param request the request
   * @return the list
   */
  @GetMapping
  public List<CertificateDtoWithoutTags> readCertificates(CertificatesRequest request) {
    return certificateService.readAll(request);
  }

  /**
   * Create certificate response entity.
   *
   * @param certificate the certificate
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<CertificateDtoWithTags> createCertificate(
      @Valid @RequestBody CertificateDtoWithTags certificate) {
    CertificateDtoWithTags createdCertificate = certificateService.create(certificate);
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
  public ResponseEntity<CertificateDtoWithTags> updateCertificatePut(
      @PathVariable long id, @Valid @RequestBody CertificateDtoWithTags certificate) {
    certificate.setId(id);
    CertificateDtoWithTags updatedCertificate = certificateService.update(certificate);
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
  public ResponseEntity<CertificateDtoWithoutTags> updateCertificatePatch(
      @PathVariable long id, @Valid @RequestBody CertificateDtoPatch certificate) {
    certificate.setId(id);
    CertificateDtoWithoutTags updatedCertificate =
        certificateService.updatePresentedFields(new CertificateDtoWithoutTags(certificate));
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
}
