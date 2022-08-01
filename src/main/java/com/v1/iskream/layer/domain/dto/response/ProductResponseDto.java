package com.v1.iskream.layer.domain.dto.response;

import com.v1.iskream.layer.domain.Product;
import com.v1.iskream.layer.domain.Thumbnail;
import com.v1.iskream.layer.domain.dto.response.PriceResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductResponseDto {
    private final Long id;
    private final List<ThumbnailResponseDto> thumbnail;
    private final String product_name_kor;
    private final String product_name_eng;
//    private int product_price;
    private final List<PriceResponseDto> prices;
    private final String product_brand;

    public ProductResponseDto(Product product, List<PriceResponseDto> prices, List<ThumbnailResponseDto> thumbnail) {
        this.id = product.getId();
        this.thumbnail = thumbnail;
        this.product_name_kor = product.getNameKor();
        this.product_name_eng = product.getNameEng();
        this.prices = prices;
        this.product_brand = product.getBrand();
    }
}
