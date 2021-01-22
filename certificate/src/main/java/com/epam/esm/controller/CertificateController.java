package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
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
  public ResponseEntity<Certificate> readCertificate(@PathVariable long id) {
    Certificate certificate = certificateService.read(id);
    return ResponseEntity.status(HttpStatus.OK).body(certificate);
  }

  @GetMapping
  public List<Certificate> readCertificates() {
    return certificateService.readAll();
  }

  /**
   * Create certificate response entity.
   *
   * @param certificate the certificate
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<Certificate> createCertificate(
      @Valid @RequestBody Certificate certificate) {

    Certificate createdCertificate = certificateService.create(certificate);
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
  public ResponseEntity<Certificate> updateCertificatePut(
      @PathVariable long id, @Valid @RequestBody Certificate certificate) {
    certificate.setId(id);
    Certificate updatedCertificate = certificateService.updatePut(certificate);
    return ResponseEntity.status(HttpStatus.OK).body(updatedCertificate);
  }

  /**
   * Update certificate patch response entity.
   *
   * @param id the id
   * @param certificate the certificate
   * @return the response entity
   */
  @PatchMapping("/{id}") // TODO refactor
  public ResponseEntity<Certificate> updateCertificatePatch(
      @PathVariable long id, @Valid @RequestBody Certificate certificate) {
    certificate.setId(id);
    Certificate updatedCertificate = certificateService.updatePatch(certificate);
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
