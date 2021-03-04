package com.jardin.api.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "images", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class Images {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  @OneToOne(mappedBy = "images")
  @JsonIgnore
  private Garment garment;

  @Column(name = "image1")
  private String linkImage1;

  @Column(name = "image2")
  private String linkImage2;

  @Column(name = "image3")
  private String linkImage3;

  @Column(name = "image4")
  private String linkImage4;

  @Column(name = "image5")
  private String linkImage5;

  @Column(name = "image6")
  private String linkImage6;

  public Images() {}

  public Images(Garment garment) {
    this.garment = garment;
  }

  public Images(Garment garment, String linkImage1) {
    this.garment = garment;
    this.linkImage1 = linkImage1;
  }

  public Images(
    Garment garment,
    String linkImage1,
    String linkImage2,
    String linkImage3,
    String linkImage4,
    String linkImage5,
    String linkImag6
  ) {
    this.garment = garment;
    this.linkImage1 = "https://jardin-products-images.s3-sa-east-1.amazonaws.com"+"/"+this.getId()+"/"+linkImage1;
    this.linkImage2 = "https://jardin-products-images.s3-sa-east-1.amazonaws.com"+"/"+this.getId()+"/"+linkImage2;
    this.linkImage3 = "https://jardin-products-images.s3-sa-east-1.amazonaws.com"+"/"+this.getId()+"/"+linkImage3;
    this.linkImage4 = "https://jardin-products-images.s3-sa-east-1.amazonaws.com"+"/"+this.getId()+"/"+linkImage4;
    this.linkImage5 = "https://jardin-products-images.s3-sa-east-1.amazonaws.com"+"/"+this.getId()+"/"+linkImage5;
    this.linkImage6 = "https://jardin-products-images.s3-sa-east-1.amazonaws.com"+"/"+this.getId()+"/"+linkImag6;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Garment getGarment() {
    return garment;
  }

  public void setGarment(Garment garment) {
    this.garment = garment;
  }

  public String getLinkImage1() {
    return linkImage1;
  }

  public void setLinkImage1(String linkImage1) {
    this.linkImage1 = linkImage1;
  }

  public String getLinkImage2() {
    return linkImage2;
  }

  public void setLinkImage2(String linkImage2) {
    this.linkImage2 = linkImage2;
  }

  public String getLinkImage3() {
    return linkImage3;
  }

  public void setLinkImage3(String linkImage3) {
    this.linkImage3 = linkImage3;
  }

  public String getLinkImage4() {
    return linkImage4;
  }

  public void setLinkImage4(String linkImage4) {
    this.linkImage4 = linkImage4;
  }

  public String getLinkImage5() {
    return linkImage5;
  }

  public void setLinkImage5(String linkImage5) {
    this.linkImage5 = linkImage5;
  }

  public String getLinkImage6() {
    return linkImage6;
  }

  public void setLinkImage6(String linkImag6) {
    this.linkImage6 = linkImag6;
  }

  @Override
  public String toString() {
    return (
      "Images{" +
      "id=" +
      id +
      ", linkImage1='" +
              "https://jardin-products-images.s3-sa-east-1.amazonaws.com"+"/"+this.getId()+"/"+linkImage1 +
      '\'' +
      ", linkImage2='" +
      linkImage2 +
      '\'' +
      ", linkImage3='" +
      linkImage3 +
      '\'' +
      ", linkImage4='" +
      linkImage4 +
      '\'' +
      ", linkImage5='" +
      linkImage5 +
      '\'' +
      ", linkImag6='" +
      linkImage6 +
      '\'' +
      '}'
    );
  }
}
