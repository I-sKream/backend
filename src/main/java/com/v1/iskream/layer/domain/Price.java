package com.v1.iskream.layer.domain;

import com.v1.iskream.layer.domain.dto.request.ProductRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    public Price(ProductRequestDto requestDto, Product product, User seller) {
        this.size = requestDto.getSize();
        this.price = requestDto.getPrice();
        this.product = product;
        this.seller = seller;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
