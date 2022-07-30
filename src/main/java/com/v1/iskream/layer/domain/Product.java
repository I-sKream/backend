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
    private String name;

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Price> prices;

    @OneToMany(mappedBy = "thumbnail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Thumbnail> thumbnails;

    public Product() {

    }

    public void addPrice(Price price){
        prices.add(price);
        price.setProduct(this);
    }
}
