package com.v1.iskream.layer.dto;

import com.v1.iskream.layer.domain.Price;
import com.v1.iskream.layer.domain.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductResponseDto {
    private final Long id;
    private final String thumbnail;
    private final String product_name;
    private final int product_price;
    private final int product_price_diff;
    private final String product_brand;

    public ProductResponseDto(Product product, Price price, int product_price_diff) {
        this.id = product.getId();
        this.thumbnail = product.getImg();
        this.product_name = product.getName();
        this.product_price = price.getPrice();
        this.product_price_diff = product_price_diff;
        this.product_brand = product.getBrand();
    }
}
