package com.v1.iskream.layer.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Thumbnail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private String url;

    public void setProduct(Product product) {
        this.product = product;
    }
}
