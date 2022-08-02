package com.v1.iskream.layer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String nameEng;

    @Column(nullable = false)
    private String nameKor;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Price> prices;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Thumbnail> thumbnails;

    public Product(Long id, String brand, String nameEng, String nameKor) {
        this.id = id;
        this.brand = brand;
        this.nameEng = nameEng;
        this.nameKor = nameKor;
    }

    public void addThumbnail(Thumbnail thumbnail){
        thumbnails.add(thumbnail);
        thumbnail.setProduct(this);
    }

    public void addPrice(Price price){
        prices.add(price);
        price.setProduct(this);
    }
}
