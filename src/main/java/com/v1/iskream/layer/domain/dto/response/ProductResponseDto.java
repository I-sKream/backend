package com.v1.iskream.layer.domain.dto.response;

import com.v1.iskream.layer.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductResponseDto {
    private Long id;
    private List<ThumbnailResponseDto> thumbnail;
    private String product_name_kor;
    private String product_name_eng;
//    private int product_price;
    private List<PriceResponseDto> prices;
    private String product_brand;

    public ProductResponseDto(Product product, List<PriceResponseDto> prices, List<ThumbnailResponseDto> thumbnail) {
        this.id = product.getId();
        this.thumbnail = thumbnail;
        this.product_name_kor = product.getNameKor();
        this.product_name_eng = product.getNameEng();
        this.prices = prices;
        this.product_brand = product.getBrand();
    }
}
