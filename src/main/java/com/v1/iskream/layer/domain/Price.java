package com.v1.iskream.layer.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Price {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private int size;

    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    private User seller;

    public void setProduct(Product product) {
        this.product = product;
    }
}
