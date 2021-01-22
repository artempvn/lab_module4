package com.epam.esm.entity;

public class CertificatesRequest {

  private String tagName;
  private String certificateName;
  private String certificateDescription;
  private SortParam sort;

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public String getCertificateName() {
    return certificateName;
  }

  public void setCertificateName(String certificateName) {
    this.certificateName = certificateName;
  }

  public String getCertificateDescription() {
    return certificateDescription;
  }

  public void setCertificateDescription(String certificateDescription) {
    this.certificateDescription = certificateDescription;
  }

  public SortParam getSort() {
    return sort;
  }

  public void setSort(SortParam sort) {
    this.sort = sort;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GetParameter{");
    sb.append("tagName='").append(tagName).append('\'');
    sb.append(", name='").append(certificateName).append('\'');
    sb.append(", description='").append(certificateDescription).append('\'');
    sb.append(", sort=").append(sort);
    sb.append('}');
    return sb.toString();
  }
}
