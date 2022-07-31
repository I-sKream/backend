package com.v1.iskream.layer.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentProductResponseDto {
    private String thumbnail;
    private String product_name_eng;
    private BigInteger product_price;
    private String product_brand;

    @Override
    public String toString() {
        return "RecentProductResponseDto{" +
                "thumbnail='" + thumbnail + '\'' +
                ", product_name_eng='" + product_name_eng + '\'' +
                ", product_price=" + product_price +
                ", product_brand='" + product_brand + '\'' +
                '}';
    }
}