package com.v1.iskream.layer.domain.dto.response;

import com.v1.iskream.layer.domain.Price;
import lombok.Getter;

@Getter
public class PriceResponseDto {
    private final int size;
    private final int price;
    private final int price_diff;

    public PriceResponseDto(Price price, int price_diff) {
        this.size = price.getSize();
        this.price = price.getPrice();
        this.price_diff = price_diff;
    }
}
