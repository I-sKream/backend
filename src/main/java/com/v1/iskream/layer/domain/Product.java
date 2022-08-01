package com.v1.iskream.layer.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
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

    public Product() {

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
