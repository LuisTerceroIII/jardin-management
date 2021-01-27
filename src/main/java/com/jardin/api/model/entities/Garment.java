package com.jardin.api.model.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "garment", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Garment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "main_color", nullable = false)
    private String mainColor;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "main_material", nullable = false)
    private String mainMaterial;

    @Column(name = "made_in")
    private String madeIn;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "comment")
    private String comment;

    @OneToOne()
    @JoinColumn(name ="images_id")
    private Images images;

    public Garment() {
    }



    public Garment(String type, String size, String mainColor, String gender, String mainMaterial, String madeIn, Integer price, String comment) {
        this.type = type;
        this.size = size;
        this.mainColor = mainColor;
        this.gender = gender;
        this.mainMaterial = mainMaterial;
        this.madeIn = madeIn;
        this.price = price;
        this.comment = comment;
    }

    public Garment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getMainColor() {
        return mainColor;
    }

    public String getGender() {
        return gender;
    }

    public String getMainMaterial() {
        return mainMaterial;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public Integer getPrice() {
        return price;
    }

    public String getComment() {
        return comment;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMainMaterial(String mainMaterial) {
        this.mainMaterial = mainMaterial;
    }

    public void setMadeIn(String madeIn) {
        this.madeIn = madeIn;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Garment{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                ", mainColor='" + mainColor + '\'' +
                ", gender='" + gender + '\'' +
                ", mainMaterial='" + mainMaterial + '\'' +
                ", madeIn='" + madeIn + '\'' +
                ", price=" + price +
                ", comment='" + comment + '\'' +
                ", images=" + images +
                '}';
    }
}